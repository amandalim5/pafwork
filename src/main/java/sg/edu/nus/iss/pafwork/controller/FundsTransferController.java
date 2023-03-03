package sg.edu.nus.iss.pafwork.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.Valid;
import sg.edu.nus.iss.pafwork.exception.TransferException;
import sg.edu.nus.iss.pafwork.model.Account;
import sg.edu.nus.iss.pafwork.model.Transfer;
import sg.edu.nus.iss.pafwork.service.FundsTransferService;
import sg.edu.nus.iss.pafwork.service.LogAuditService;

@Controller
@RequestMapping(path = "/")
public class FundsTransferController {
  @Autowired
  FundsTransferService fundService;
  @Autowired
  LogAuditService logAuditService;
  @GetMapping
  public String getTransferFrom(Model model){

    List<Account> result = fundService.readAll();
    Transfer t = new Transfer();
    model.addAttribute("accounts", result);
    model.addAttribute("transfer", t);
    return "index";
  }
  @PostMapping(path = "/transfer", consumes = "application/x-www-form-urlencoded", produces = "text/html")
  public String returnTransferForm(@Valid @ModelAttribute("transfer") Transfer transfer, BindingResult bind, Model model){
    List<Account> result = fundService.readAll();
    model.addAttribute("accounts", result);
    model.addAttribute("transfer", transfer);
    // addressing C0
    Boolean fromAccountExists = false;
    Boolean toAccountExists = false;
    for(Account a: result){
      System.out.println("testing: " + a.getAccountId());
      if(a.getAccountId().equals(transfer.getFromAccount())){
        fromAccountExists = true;
        System.out.println("setting the fromAccountExists to true");
      }
      if(a.getAccountId().equals(transfer.getToAccount())){
        toAccountExists = true;
        System.out.println("setting the toAccountExists to true");
      }
    }
    if(!fromAccountExists || !toAccountExists){
      System.out.println("One or more of your accounts do not exist.");
      ObjectError err = new ObjectError("globalError", "One or more of the accounts do not exist.");
      bind.addError(err);
      return "index";
    }

    // addressing C1
    if(bind.hasErrors()){
      model.addAttribute("transfer", transfer);
      return "index";
    } 
    // addressing C2
    if(transfer.getFromAccount().equals(transfer.getToAccount())){
      System.out.println("the accounts were the same...");
      ObjectError err = new ObjectError("globalError", "The accounts cannot be the same.");
      bind.addError(err);
      return "index";
    }
    // addressing C3
    if(transfer.getAmount().doubleValue()<=0){
      System.out.println("Please put an amount greater than 0.");
      ObjectError err = new ObjectError("globalError", "Please put an amount greater than 0.");
      bind.addError(err);
      return "index";
    }
    // addressing C4
    if(transfer.getAmount().doubleValue()<10){
      System.out.println("The minimum transfer amount is $10.");
      ObjectError err = new ObjectError("globalError", "The minimum transfer amount is $10.");
      bind.addError(err);
      return "index";
    }
    // addressing C5
    Double amountAvailable = null;
    Double amountBeforeAdding = null;
    String fromName = null;
    String toName = null;
    for(Account a: result){
      if(a.getAccountId().equals(transfer.getFromAccount())){
        amountAvailable = a.getBalance().doubleValue();
        System.out.println("the amount in the account is " + amountAvailable);
        fromName = a.getCname();
      }
      if(a.getAccountId().equals(transfer.getToAccount())){
        amountBeforeAdding = a.getBalance().doubleValue();
        toName = a.getCname();
      }
    }
    Double amountRequested = transfer.getAmount().doubleValue();
    if(amountAvailable<amountRequested){
      System.out.println("the person does not have enough...");
      ObjectError err = new ObjectError("globalError", "You only have $" + amountAvailable);
      bind.addError(err);
      return "index";
    }

    // proceed with fund transfer
    System.out.println("from this number..." + amountRequested);
    BigDecimal bd = new BigDecimal(amountRequested).setScale(2,RoundingMode.HALF_DOWN);
    
    amountRequested = bd.doubleValue();
    System.out.println("to this number..." + amountRequested);
    Double amountAfterDeducting = amountAvailable - amountRequested;
    Double amountAfterAdding = amountBeforeAdding + amountRequested;
    try {
      fundService.transferMoney(transfer, amountAfterDeducting, amountAfterAdding);
    } catch (TransferException e) {
      return "index";
      
    }

    // log the transfer details into redis
    JsonObjectBuilder transferLog = Json.createObjectBuilder();

    transferLog
      .add("transactionId", transfer.getTransferId())
      .add("date", java.time.LocalDate.now().toString())
      .add("from_account", transfer.getFromAccount())
      .add("to_account", transfer.getToAccount())
      .add("amount", transfer.getAmount());

    JsonObject r = transferLog.build();
    System.out.println(r.toString());
    logAuditService.add(transfer.getTransferId(),r);

    model.addAttribute("id", transfer.getTransferId());
    model.addAttribute("fromName", fromName);
    model.addAttribute("toName", toName);
    return "transfer";
  }
}
