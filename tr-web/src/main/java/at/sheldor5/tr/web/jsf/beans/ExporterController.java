package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.exporter.ExporterManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;

/**
 * @author Michael Palata
 * @date 10.06.2017
 */
@Named("exporter")
@ApplicationScoped
public class ExporterController {

  public List<ExporterPlugin> getPlugins() {
    return ExporterManager.getInstance().getPlugins();
  }

}
