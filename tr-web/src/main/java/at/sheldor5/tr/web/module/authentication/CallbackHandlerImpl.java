package at.sheldor5.tr.web.module.authentication;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class CallbackHandlerImpl implements CallbackHandler {

  private static final Logger LOGGER = Logger.getLogger(CallbackHandlerImpl.class.getName());

  private String username;
  private String password;

  public CallbackHandlerImpl() {
    LOGGER.info("CallbackHandlerImpl()");
  }

  public CallbackHandlerImpl(String username, String password) {
    this.username = username;
    this.password = password;
    LOGGER.info("CallbackHandlerImpl()");
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    // Nothing to do since we already have the username and password
    LOGGER.info("CallbackHandlerImpl#handle()");
  }

  @Override
  public String toString() {
    return String.format("@s {@s:@s}", super.toString(), username, password);
  }
}
