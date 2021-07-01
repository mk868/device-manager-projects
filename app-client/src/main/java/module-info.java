module root.app.client {
  requires javafx.base;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires javax.inject;
  requires org.kordamp.ikonli.core;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.ikonli.fontawesome5;
  requires com.google.guice;
  requires org.slf4j;
  requires root.message.client;
  requires root.core.message;
  requires root.core.communication.serial;
  // add icon pack modules
  exports mk.dm.app.client;
  opens mk.dm.app.client.service to com.google.guice;
  opens mk.dm.app.client to javafx.graphics, com.google.guice;
  exports mk.dm.app.client.controller;
  opens mk.dm.app.client.controller to javafx.fxml, com.google.guice;
  exports mk.dm.app.client.fragment;
  opens mk.dm.app.client.fragment to javafx.graphics, com.google.guice;
  exports mk.dm.app.client.controller.content;
  opens mk.dm.app.client.controller.content to com.google.guice, javafx.fxml;
  exports mk.dm.app.client.guice;
  opens mk.dm.app.client.guice to com.google.guice, javafx.graphics;
  exports mk.dm.app.client.model;
  opens mk.dm.app.client.model to com.google.guice, javafx.base, javafx.graphics;
}
