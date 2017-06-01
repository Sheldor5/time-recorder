package at.sheldor5.tr.web.module.authentication;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.core.auth.AuthenticationManager;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class LoginModuleImpl implements LoginModule {

  private static final Logger LOGGER = Logger.getLogger(LoginModuleImpl.class.getName());

  private static final AuthenticationManager AUTHENTICATION_MANAGER = AuthenticationManager.getInstance();

  private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile("(?i)\\bselect|create|insert|delete|drop|update|or|%.\\d\\b");

  private Subject subject;
  private CallbackHandler callbackHandler;
  private Map sharedState;
  private Map options;

  public LoginModuleImpl() {
    LOGGER.info("LoginModuleImpl()");
  }

  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
    LOGGER.info("initialize");
    this.subject = subject;
    this.callbackHandler = callbackHandler;
    this.sharedState = sharedState;
    this.options = options;
  }

  @Override
  public boolean login() throws LoginException {
    LOGGER.info("LoginModuleImpl#login()");

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("username");
    callbacks[1] = new PasswordCallback("password", true);

    try {
      callbackHandler.handle(callbacks);
    } catch (Exception e) {
      e.printStackTrace();
    }

    final String username = ((NameCallback) callbacks[0]).getName();
    final String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());

    // detect attackers
    final Matcher matcher = SQL_INJECTION_PATTERN.matcher(username);

    if (matcher.find()) {
      //someone tries to break into this application and should be tracked back
      LOGGER.warning("SQL Injection attempt detected: username was altered to include SQL keyword: " + matcher.group());
      return false;
    }

    // Perform credential realization and credential authentication
    // @TODO
    final User user = AUTHENTICATION_MANAGER.getUser(username, password);

    return user != null;
  }

  @Override
  public boolean commit() throws LoginException {
    return false;
  }

  @Override
  public boolean abort() throws LoginException {
    return false;
  }

  @Override
  public boolean logout() throws LoginException {
    return false;
  }
}
