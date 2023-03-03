package sg.edu.nus.iss.pafwork.model;

import java.math.BigDecimal;

import org.springframework.jdbc.support.rowset.SqlRowSet;



public class Account {

  private String accountId;
  private String cname;
  private BigDecimal balance;
  private String show;

  public String getShow() {
    return show;
  }
  public void setShow(String show) {
    this.show = show;
  }
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
  public String getCname() {
    return cname;
  }
  public void setCname(String cname) {
    this.cname = cname;
  }
  public BigDecimal getBalance() {
    return balance;
  }
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public static Account populate(SqlRowSet rs){
    final Account a = new Account();

    return a;

  }
  
}
