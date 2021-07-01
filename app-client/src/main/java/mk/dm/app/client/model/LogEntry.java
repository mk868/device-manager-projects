package mk.dm.app.client.model;

import javafx.beans.property.SimpleStringProperty;

public class LogEntry {

  private final SimpleStringProperty date = new SimpleStringProperty("");
  private final SimpleStringProperty type = new SimpleStringProperty("");
  private final SimpleStringProperty message = new SimpleStringProperty("");

  public LogEntry() {
  }

  public LogEntry(String date, String type, String message) {
    setDate(date);
    setType(type);
    setMessage(message);
  }

  public String getDate() {
    return date.get();
  }

  public SimpleStringProperty dateProperty() {
    return date;
  }

  public void setDate(String date) {
    this.date.set(date);
  }

  public String getType() {
    return type.get();
  }

  public SimpleStringProperty typeProperty() {
    return type;
  }

  public void setType(String type) {
    this.type.set(type);
  }

  public String getMessage() {
    return message.get();
  }

  public SimpleStringProperty messageProperty() {
    return message;
  }

  public void setMessage(String message) {
    this.message.set(message);
  }
}
