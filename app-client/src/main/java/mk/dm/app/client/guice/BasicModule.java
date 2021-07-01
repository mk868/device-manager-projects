package mk.dm.app.client.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import mk.dm.app.client.controller.ContentController;

public class BasicModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ContentController.class).in(Scopes.SINGLETON);
  }
}
