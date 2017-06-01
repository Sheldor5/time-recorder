package at.sheldor5.tr.web.module.authentication;

import javax.security.auth.callback.Callback;
import java.util.logging.Logger;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class CallbackImpl implements Callback {

  private static final Logger LOGGER = Logger.getLogger(CallbackImpl.class.getName());

  public CallbackImpl() {
    LOGGER.warning("CallbackImpl()");
  }
}
