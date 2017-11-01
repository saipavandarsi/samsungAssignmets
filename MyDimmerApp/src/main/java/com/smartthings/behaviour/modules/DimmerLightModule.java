package com.smartthings.behaviour.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.smartthings.behaviour.beans.DimmerLight;
import com.smartthings.ratpack.handlers.DimmerLightGetHandler;
import com.smartthings.ratpack.handlers.DimmerLightPostHandler;
import com.smartthings.ratpack.handlers.LoggingHandler;

import ratpack.handling.HandlerDecorator;

/**
 * An example Guice module.
 */
public class DimmerLightModule extends AbstractModule {

  /**
   * Adds a service impl to the application, and registers a decorator so that all requests are logged.
   * Registered implementations of {@link ratpack.handling.HandlerDecorator} are able to decorate the application handler.
   *
   * @see MyHandler
   */
  protected void configure() {
	bind(DimmerLightPostHandler.class);
	bind(DimmerLightGetHandler.class);
	bind(DimmerLight.class);
    Multibinder.newSetBinder(binder(), HandlerDecorator.class).addBinding().toInstance(HandlerDecorator.prepend(new LoggingHandler()));
  }
}
