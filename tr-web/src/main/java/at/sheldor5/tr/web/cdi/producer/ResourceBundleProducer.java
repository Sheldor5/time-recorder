package at.sheldor5.tr.web.cdi.producer;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import java.util.PropertyResourceBundle;

/**
 * @author Michael Palata
 * @date 15.06.2017
 */
public class ResourceBundleProducer {

  @Produces
  public PropertyResourceBundle getResourceBundle() {
    FacesContext context = FacesContext.getCurrentInstance();
    PropertyResourceBundle result = context.getApplication().evaluateExpressionGet(context, "#{msg}", PropertyResourceBundle.class);
    return result;
  }
}
