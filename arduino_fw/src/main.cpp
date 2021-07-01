#include <Arduino.h>

const int BUFFER_LEN = 256;
const uint8_t LED_PIN = LED_BUILTIN;

const uint16_t MESSAGE_ENABLE_LOGGER = 0x0035;
const uint16_t MESSAGE_ERROR_LOG = 0x0002;
const uint16_t MESSAGE_INFO_LOG = 0x0001;
const uint16_t MESSAGE_UPDATE_BLINK_PARAMS = 0x0033;

static uint64_t led_last_change = 0;

static bool logger_enabled = 1;
static uint32_t led_time_on = 1000;
static uint32_t led_time_off = 1000;

static void log_info(String message);
static void log_error(String message);
static void parse_message(uint8_t *buff, int len);

void setup()
{
  Serial.begin(115200);
  Serial.setTimeout(10);

  pinMode(LED_PIN, OUTPUT);

  cli();
  // set timer1 interrupt at 1kHz
  TCCR1A = 0;              // set entire TCCR1A register to 0
  TCCR1B = 0;              // same for TCCR1B
  TCNT1 = 0;               //initialize counter value to 0;
  OCR1A = 1999;            // = (16_000_000 Hz) / (1000 Hz * 8) - 1
  TCCR1B |= (1 << WGM12);  // CTC mode
  TCCR1B |= (1 << CS11);   // prescaler 8
  TIMSK1 |= (1 << OCIE1A); // compare interrupt
  sei();

  log_info("Device inited");
}

void loop()
{
  uint8_t buff[200];

  size_t size = Serial.readBytes(buff, sizeof(buff));
  if (size > 0)
  {
    parse_message(buff, size);
  }
}

ISR(TIMER1_COMPA_vect)
{
  unsigned long now = millis();
  bool enabled = digitalRead(LED_PIN);

  if (enabled && (now - led_time_on > led_last_change))
  {
    digitalWrite(LED_PIN, 0);
    led_last_change = now;
  }
  else if (!enabled && (now - led_time_off > led_last_change))
  {
    digitalWrite(LED_PIN, 1);
    led_last_change = now;
  }
}

static void log(uint16_t message_id, String text)
{
  if (!logger_enabled)
  {
    return;
  }

  uint8_t buffer[BUFFER_LEN];
  size_t size = 0;
  // message id
  buffer[0] = (message_id >> 8) & 0xFF;
  buffer[1] = message_id & 0xFF;
  size += 2;

  // text length
  buffer[2] = 0;
  buffer[3] = 0;
  buffer[4] = 0;
  buffer[5] = text.length();
  size += 4;

  // text
  text.getBytes(buffer + size, sizeof(buffer) - size);
  size += text.length();

  Serial.write(buffer, size);
  Serial.flush();
  delay(200);
}

static void log_info(String text)
{
  log(MESSAGE_INFO_LOG, text);
}

static void log_error(String text)
{
  log(MESSAGE_ERROR_LOG, text);
}

static void parse_message(uint8_t *buff, int len)
{
  uint16_t message_id = ((uint16_t)buff[0] << 8) | buff[1];

  if (message_id == MESSAGE_ENABLE_LOGGER)
  {
    bool enable = buff[2];
    if (enable)
    {
      logger_enabled = true;
    }
    log_info(String("ENABLE_LOGGER: ") + (enable ? '1' : '0'));
    if (!enable)
    {
      logger_enabled = false;
    }
  }
  if (message_id == MESSAGE_UPDATE_BLINK_PARAMS)
  {
    uint32_t time_on = ((uint32_t)buff[2] << 24) | ((uint32_t)buff[3] << 16) | ((uint32_t)buff[4] << 8) | buff[5];
    uint32_t time_off = ((uint32_t)buff[6] << 24) | ((uint32_t)buff[7] << 16) | ((uint32_t)buff[8] << 8) | buff[9];

    log_info("UPDATE_BLINK_PARAMS");
    log_info(String("time on: ") + time_on);
    log_info(String("time off: ") + time_off);
    if (time_on < 10)
    {
      log_error("time on >= 10!");
    }
    else if (time_off < 10)
    {
      log_error("time off >= 10!");
    }
    else
    {
      led_time_on = time_on;
      led_time_off = time_off;
      log_info("LED: OK");
    }
  }
}
