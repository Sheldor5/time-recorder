package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.web.BusinessLayer;
import at.sheldor5.tr.web.jsf.converter.LocalTimeConverter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
  private List<String> months = new ArrayList<>();

  @Inject
  public FlexitimeAccountController(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
  }

  public FlexitimeAccountController() {
    // CDI
  }

  @PostConstruct
  private void getAccoutnData() {
    LocalDate now = LocalDate.now();
    DateFormatSymbols dfs = getDateFormatSymbols();
    LocalDate currentDate;
    Account currentAccount;
    String str = "";
    for(int i = 0; i < now.getMonthValue(); i++) {
      currentDate = LocalDate.of(now.getYear(), i + 1, 1);
      currentAccount = businessLayer.getAccountOfMonth(currentDate);
      if(currentAccount != null) {
        str = convertToHours(currentAccount.getTime());
      }
      else {
        str = "/";
      }
      months.add(dfs.getMonths()[i] + ": " + str);
    }
  }

  private String convertToHours(long time) {
    boolean isNegative = false;
    if(time < 0) {
      isNegative = true;
    }
    time = Math.abs(time);
    return (isNegative ? "-" : "") + LocalTime.MIN.plusSeconds(time).toString();
  }

  private DateFormatSymbols getDateFormatSymbols() {
    return new DateFormatSymbols(Locale.GERMANY);
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

  public List<String> getMonths() {
    return months;
  }

  public void setMonths(List<String> months) {
    this.months = months;
  }
}
