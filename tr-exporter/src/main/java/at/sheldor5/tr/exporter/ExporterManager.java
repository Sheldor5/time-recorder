package at.sheldor5.tr.exporter;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.plugins.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ExporterManager implements PluginManager<ExporterPlugin> {

  private static final Logger LOGGER = Logger.getLogger(ExporterManager.class.getName());

  private static ExporterManager instance;

  public static ExporterManager getInstance() {
    if (instance == null) {
      instance = new ExporterManager();
    }
    return instance;
  }

  final Map<String, ExporterPlugin> plugins = new HashMap<>();

  @Override
  public void initialize() {

  }

  @Override
  public void addPlugin(final ExporterPlugin plugin) {
    plugins.put(plugin.getName(), plugin);
  }

  @Override
  public void addPlugins(final List<ExporterPlugin> plugins) {
    plugins.addAll(plugins);
  }

  @Override
  public ExporterPlugin getPlugin(final String name) {
    return plugins.get(name);
  }

  @Override
  public List<ExporterPlugin> getPlugins() {
    return new ArrayList<>(plugins.values());
  }
}