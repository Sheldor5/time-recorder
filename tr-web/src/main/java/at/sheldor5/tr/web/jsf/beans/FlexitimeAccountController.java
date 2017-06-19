package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.web.BusinessLayer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
  private UserController userController;

  private String overallBalance;
  private List<String> monthNames = new ArrayList<>();
  private List<String> monthTimes = new ArrayList<>();
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");


  @Inject
  public FlexitimeAccountController(final BusinessLayer businessLayer, final UserController userController) {
    this.businessLayer = businessLayer;
    this.userController = userController;
  }

  public FlexitimeAccountController() {
    // CDI
  }

  @PostConstruct
  private void getAccoutnData() {
    LocalDate now = LocalDate.now();
    createNewAccountObjectIfNecessary(now);
    DateFormatSymbols dfs = getDateFormatSymbols();
    monthNames = Arrays.asList(dfs.getMonths());
    LocalDate currentDate;
    Account currentAccount;
    String timeDisplay = "";
    long totalTime = 0;
    for(int i = 0; i < now.getMonthValue(); i++) {
      currentDate = LocalDate.of(now.getYear(), i + 1, 1);
      currentAccount = businessLayer.getAccountOfMonth(currentDate);
      if(currentAccount != null) {
        timeDisplay = convertToHours(currentAccount.getTime());
        totalTime += currentAccount.getTime();
      }
      else {
        timeDisplay = "/";
      }
      monthTimes.add(timeDisplay);
    }
    overallBalance = convertToHours(totalTime);
  }

  private void createNewAccountObjectIfNecessary(LocalDate now) {
    Account accountOfMonth = businessLayer.getAccountOfMonth(now);
    if(accountOfMonth == null || accountOfMonth.getDate().getMonthValue() != now.getMonthValue()) {
      // it's already the next month, so create a new account object
      accountOfMonth = new Account(now);
      accountOfMonth.setUserMapping(userController.getUserMapping());
      accountOfMonth.setTime(0);
      accountOfMonth.setTimeWorked(0);
      businessLayer.save(accountOfMonth, false);
      businessLayer.updateScheduleAccounts(now, businessLayer.getSchedules());
    }
  }

  private String convertToHours(long time) {
    boolean isNegative = false;
    if(time < 0) {
      isNegative = true;
    }
    time = Math.abs(time);
    return (isNegative ? "-" : "") + LocalTime.MIN.plusSeconds(time).format(formatter);
  }

  private DateFormatSymbols getDateFormatSymbols() {
    return new DateFormatSymbols(Locale.GERMANY);
  }

  public String getOverallBalance() {
    return overallBalance;
  }

  public void setOverallBalance(String overallBalance) {
    this.overallBalance = overallBalance;
  }

  public List<String> getMonthTimes() {
    return monthTimes;
  }

  public void setMonthTimes(List<String> monthTimes) {
    this.monthTimes = monthTimes;
  }

  public List<String> getMonthNames() {
    return monthNames;
  }

  public void setMonthNames(List<String> monthNames) {
    this.monthNames = monthNames;
  }
}
