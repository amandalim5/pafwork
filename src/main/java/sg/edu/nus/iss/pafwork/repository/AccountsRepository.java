package sg.edu.nus.iss.pafwork.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.pafwork.model.Account;
import sg.edu.nus.iss.pafwork.model.Transfer;

@Repository
public class AccountsRepository implements FundRepository {
  @Autowired 
  private JdbcTemplate jdbcTemplate;

  String readAllSql = "select * from accounts";
  String deduct = "update accounts set balance = ? where account_id = ?";

  @Override
  public List<Account> readAll() {
    final SqlRowSet rs = jdbcTemplate.queryForRowSet(readAllSql);
    List<Account> result = new ArrayList<Account>();
    while(rs.next()){
      Account a = new Account();
      a.setAccountId(rs.getString("account_id"));
      System.out.println("the account id is " + rs.getString("account_id"));
      a.setCname(rs.getString("cname"));
      a.setBalance(rs.getBigDecimal("balance"));
      a.setShow(a.getCname() + " (" + a.getAccountId() + ")");
      // System.out.println("the amount in their account is " + rs.getBigDecimal("balance"));
      // System.out.println(rs.getBigDecimal("balance").doubleValue() + 100);
      result.add(a);
    }
    return result;

  }

  @Override
  public int deducting(Transfer t, Double a) {
    int updated = jdbcTemplate.update(deduct,a, t.getFromAccount());

    // deduct from the fromAccount
    return updated;
  }

  @Override
  public int adding(Transfer t, Double a) {
    int updated = jdbcTemplate.update(deduct, a, t.getToAccount());
    return updated;
  }

  


}
