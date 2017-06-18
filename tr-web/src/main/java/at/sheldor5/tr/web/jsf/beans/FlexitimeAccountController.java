package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.web.BusinessLayer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * @author Consti
 * @date 17.06.2017
 */
@Named(value = "flexitimeAccount")
@RequestScoped
public class FlexitimeAccountController {

  private static final Logger LOGGER = Logger.getLogger(FlexitimeAccountController.class.getName());

  private BusinessLayer businessLayer;

  private long overallBalance;
  private long monthBalance;

  @Inject
  public FlexitimeAccountController(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
  }

  public FlexitimeAccountController() {
    // CDI
  }

  @PostConstruct
  private void getAccoutnData() {
    Account yearAccount = businessLayer.getAccountOfYear(LocalDate.now());
    Account monthAccount = businessLayer.getAccountOfMonth(LocalDate.now());
//    Account yearAccount2 = businessLayer.getAccountOfYear(LocalDate.now().plusYears(1));
//    Account monthAccount2 = businessLayer.getAccountOfMonth(LocalDate.now().plusMonths(1));
//    Account yearAccount3 = businessLayer.getAccountOfYear(LocalDate.now().minusYears(1));
//    Account monthAccount3 = businessLayer.getAccountOfMonth(LocalDate.now().minusMonths(1));
//    businessLayer.save
    monthBalance = monthAccount.getTime();
    overallBalance = yearAccount.getTime();
  }

  public long getOverallBalance() {
    return overallBalance;
  }

  public void setOverallBalance(long overallBalance) {
    this.overallBalance = overallBalance;
  }

  public long getMonthBalance() {
    return monthBalance;
  }

  public void setMonthBalance(long monthBalance) {
    this.monthBalance = monthBalance;
  }
}
