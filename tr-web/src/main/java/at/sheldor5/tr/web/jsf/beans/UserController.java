package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author Michael Palata
 * @date 02.06.2017
 */
@ManagedBean(name = "user")
@SessionScoped
public class UserController extends User {
}
