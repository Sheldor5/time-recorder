package at.sheldor5.tr.web.module.authentication;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class Login {
  public static Subject login(String username, String password)
      throws LoginException {
    CallbackHandlerImpl callbackHandler = new CallbackHandlerImpl(username, password);
    LoginContext context = new LoginContext("auth-context", callbackHandler);
    context.login();
    return context.getSubject();
  }
}
