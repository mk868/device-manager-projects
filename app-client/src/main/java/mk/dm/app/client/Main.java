package mk.dm.app.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mk.dm.app.client.controller.ContentController;
import mk.dm.app.client.guice.BasicModule;
import mk.dm.app.client.model.AppConfig;
import mk.dm.app.client.service.DeviceService;

public class Main extends Application {

  private Injector injector;
  private Stage primaryStage;
  private AppConfig appConfig;
  private DeviceService deviceService;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() throws Exception {
    var params = this.getParameters();
    appConfig = new AppConfig();
    appConfig.setDebugUI(params.getRaw().contains(AppArgs.RAW_DEBUG_UI));

    injector = Guice.createInjector(
        new BasicModule(),
        binder -> {
          binder.bind(Application.class).toProvider(() -> this);
          binder.bind(Stage.class).toProvider(() -> primaryStage);
          binder.bind(AppConfig.class).toProvider(() -> appConfig);
        }
    );
    deviceService = injector.getInstance(DeviceService.class);
  }

  @Override
  public void start(Stage stage) throws IOException {
    this.primaryStage = stage;
    var loader = new FXMLLoader(getClass().getResource("/fxml/main-layout.fxml"));
    loader.setControllerFactory(injector::getInstance);
    Parent root = loader.load();
    injector.getInstance(ContentController.class).load(AppFragments.MAIN);

    var scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
    stage.setMinHeight(450);
    stage.setMinWidth(600);
    // TODO stage.setTitle("");
    stage.setScene(scene);
    stage.show();
    deviceService.start();
  }

  @Override
  public void stop() throws Exception {
    super.stop();

    deviceService.stop();
  }
}
