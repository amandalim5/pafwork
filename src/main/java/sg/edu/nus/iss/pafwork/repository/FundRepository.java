package sg.edu.nus.iss.pafwork.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.pafwork.model.Account;
import sg.edu.nus.iss.pafwork.model.Transfer;

@Repository
public interface FundRepository {
  // read all accounts
  List<Account> readAll();

  int deducting(Transfer t, Double a);
  int adding(Transfer t, Double a);
}
