package at.sheldor5.tr.web.module.authentication;

import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class LoginModuleImpl implements LoginModule {

  private static final Logger LOGGER = Logger.getLogger(LoginModuleImpl.class.getName());

  private static final AuthenticationManager AUTHENTICATION_MANAGER = AuthenticationManager.getInstance();

  private static final Map<String, UserMapping> USER_MAPPING_MAP = new HashMap<>();

  private Subject subject;
  private CallbackHandler callbackHandler;
  private Map sharedState;
  private Map options;
  private UserPrincipal userPrincipal;
  private RolePrincipal rolePrincipal;
  private String username;
  private List<Role> roles = new ArrayList<>();

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

    username = ((NameCallback) callbacks[0]).getName();
    final String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());

    // Perform credential realization and credential authentication
    final UserMapping userMapping = AUTHENTICATION_MANAGER.getUserMapping(username, password);

    if (userMapping == null) {
      //return false;
      throw new LoginException("Authentication failed");
    }

    final Role role = userMapping.getRole();
    if (role != null) {
      roles.add(role);
      Collections.addAll(roles, role.getImplies());
    }

    USER_MAPPING_MAP.put(username, userMapping);

    return true;
  }

  @Override
  public boolean commit() throws LoginException {
    userPrincipal = new UserPrincipal(username);
    subject.getPrincipals().add(userPrincipal);

    if (roles.size() > 0) {
      for (final Role role : roles) {
        rolePrincipal = new RolePrincipal(role);
        subject.getPrincipals().add(rolePrincipal);
      }
    }

    return true;
  }

  public static UserMapping getAuthenticatedUserMapping(final String username) {
    return USER_MAPPING_MAP.remove(username);
  }

  @Override
  public boolean abort() throws LoginException {
    return false;
  }

  @Override
  public boolean logout() throws LoginException {
    subject.getPrincipals().remove(userPrincipal);
    subject.getPrincipals().remove(rolePrincipal);
    return true;
  }
}
