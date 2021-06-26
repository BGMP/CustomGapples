package cl.bgmp.customgapples.injection;

import cl.bgmp.customgapples.Config;
import cl.bgmp.customgapples.CustomGapples;
import cl.bgmp.customgapples.CustomGapplesConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CustomGapplesModule extends AbstractModule {
  private CustomGapples customGapples;
  private CustomGapplesConfig config;

  public CustomGapplesModule(CustomGapples customGapples, CustomGapplesConfig config) {
    this.customGapples = customGapples;
    this.config = config;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(CustomGapples.class).toInstance(this.customGapples);
    this.bind(Config.class).toInstance(this.config);
  }
}
