package mk.dm.app.client.controller;

import com.google.inject.Injector;
import java.io.IOException;
import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.inject.Inject;
import mk.dm.app.client.fragment.Fragment;
import mk.dm.app.client.fragment.FragmentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentController implements FragmentLoader {

  private static final Logger log = LoggerFactory.getLogger(ContentController.class);
  private static final double REPLACE_TIME = 100;

  private final Injector injector;
  private Fragment<?> topFragment;
  private Object topController;
  @FXML
  private StackPane stackPane;

  @Inject
  public ContentController(Injector injector) {
    this.injector = injector;
  }

  protected FXMLLoader getLoader(String name) {
    var loader = new FXMLLoader(getClass().getResource(name));
    loader.setControllerFactory(injector::getInstance);
    return loader;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T load(Fragment<T> fragment) throws IOException {
    log.info("FragmentLoader.load({})", fragment);
    String resource = fragment.resource();

    if (Objects.equals(topFragment, fragment)) {
      log.info("Fragment already loaded");
      // method invoked in single thread, cast is safe
      if (topController.getClass() != fragment.controllerClass()) {
        throw new ClassCastException(
            "Could not cast controller class, expected: " + fragment.controllerClass().getName()
                + ", cached: " + topController.getClass().getName());
      }
      return (T) topController;
    }

    var loader = getLoader(resource);
    addToView(loader.load());

    var controller = loader.<T>getController();
    if (controller.getClass() != fragment.controllerClass()) {
      throw new ClassCastException(
          "Could not cast controller class, expected: " + fragment.controllerClass().getName()
              + ", got: " + controller.getClass().getName());
    }

    topFragment = fragment;
    topController = controller;

    return controller;
  }

  protected void addToView(Node node) {
    stackPane.getChildren().add(node);
    if (stackPane.getChildren().size() == 1) {
      log.info("stackPane has one children");
      return;
    }
    // lets remove extra children
    while (stackPane.getChildren().size() > 2) {
      stackPane.getChildren().remove(0);
    }
    Node background = stackPane.getChildren().get(0);

    animate(background, node);

    log.info("stackPane children: {}", stackPane.getChildren().size());
  }

  protected void animate(Node oldNode, Node newNode) {
    var timeline = new Timeline(
        new KeyFrame(Duration.millis(REPLACE_TIME), new KeyValue(oldNode.opacityProperty(), 0.0)),
        new KeyFrame(Duration.millis(0), new KeyValue(newNode.opacityProperty(), 0.0)),
        new KeyFrame(Duration.millis(REPLACE_TIME), new KeyValue(newNode.opacityProperty(), 0.0)),
        new KeyFrame(Duration.millis(REPLACE_TIME * 2),
            new KeyValue(newNode.opacityProperty(), 1.0))
    );
    timeline.play();
  }
}
