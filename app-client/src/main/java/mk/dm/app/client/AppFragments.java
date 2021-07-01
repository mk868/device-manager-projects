package mk.dm.app.client;

import mk.dm.app.client.fragment.Fragment;
import mk.dm.app.client.controller.content.MainPageController;
import mk.dm.app.client.controller.content.SettingsPageController;
import mk.dm.app.client.controller.content.LogsPageController;

public class AppFragments {

  private AppFragments() {
  }

  public static final Fragment<MainPageController> MAIN =
      new Fragment<>("/fxml/content/main-page.fxml", MainPageController.class);
  public static final Fragment<SettingsPageController> SETTINGS =
      new Fragment<>("/fxml/content/settings-page.fxml", SettingsPageController.class);
  public static final Fragment<LogsPageController> LOGS =
      new Fragment<>("/fxml/content/logs-page.fxml", LogsPageController.class);

}
