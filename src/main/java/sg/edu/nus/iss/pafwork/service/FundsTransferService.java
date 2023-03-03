package sg.edu.nus.iss.pafwork.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.pafwork.exception.TransferException;
import sg.edu.nus.iss.pafwork.model.Account;
import sg.edu.nus.iss.pafwork.model.Transfer;
import sg.edu.nus.iss.pafwork.repository.AccountsRepository;

@Service
public class FundsTransferService {
  @Autowired
  AccountsRepository fundRepoImpl;
  public List<Account> readAll(){
    return fundRepoImpl.readAll();
  }

  @Transactional(rollbackFor = TransferException.class)
  public void transferMoney(Transfer transfer, Double amountAfterDeducting, Double amountAfterAdding) throws TransferException{
    String transferId = UUID.randomUUID().toString().substring(0, 8);
    transfer.setTransferId(transferId);

    int firstStep = fundRepoImpl.deducting(transfer, amountAfterDeducting);
    System.out.println("the result of the first! " + firstStep);
    int secondStep = fundRepoImpl.adding(transfer, amountAfterAdding);
    System.out.println("the result of the second! " + secondStep);

    if(firstStep!=1 || secondStep!=1){
      throw new TransferException("cannot complete the transaction!");

    } else{
      System.out.println("we did it:)");
    }

  }
}
