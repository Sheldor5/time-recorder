package at.sheldor5.tr.api.plugins;

import java.util.List;

public interface PluginManager<Type extends Plugin> {

  void initialize();

  void addPlugin(Type plugin);

  void addPlugins(List<Type> plugins);

  Type getPlugin(final String name);

  List<Type> getPlugins();
}