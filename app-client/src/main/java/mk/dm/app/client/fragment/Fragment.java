package mk.dm.app.client.fragment;

/**
 * Describes a dynamically replaced portion of the user interface. This record is used because it
 * provides generic information about controller type.
 *
 * @param <T> type of the controller
 */
public record Fragment<T>(String resource, Class<T> controllerClass) {

}
