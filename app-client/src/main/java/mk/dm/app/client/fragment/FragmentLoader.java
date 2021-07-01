package mk.dm.app.client.fragment;

import java.io.IOException;

/**
 * A controller that wants to handle fragment replacement should implement this interface.
 */
public interface FragmentLoader {

  /**
   * Try to load fragment
   *
   * @param fragment object with fragment description
   * @param <T>      type of the controller
   * @return MVC controller object
   * @throws IOException in case of {@link javafx.fxml.FXMLLoader} error
   */
  <T> T load(Fragment<T> fragment) throws IOException;
}
