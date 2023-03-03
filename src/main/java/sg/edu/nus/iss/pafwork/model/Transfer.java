package sg.edu.nus.iss.pafwork.model;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class Transfer {
  // addressing C1
  @Size(min = 10, message = "The account ID should be 10 characters long.")
  private String fromAccount;
  @Size(min = 10, message = "The account ID should be 10 characters long.")
  private String toAccount;

  private BigDecimal amount;
  private String comments;
  private String transferId;

  public String getTransferId() {
    return transferId;
  }
  public void setTransferId(String transferId) {
    this.transferId = transferId;
  }
  public String getFromAccount() {
    return fromAccount;
  }
  public void setFromAccount(String fromAccount) {
    this.fromAccount = fromAccount;
  }
  public String getToAccount() {
    return toAccount;
  }
  public void setToAccount(String toAccount) {
    this.toAccount = toAccount;
  }
  public BigDecimal getAmount() {
    return amount;
  }
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
  public String getComments() {
    return comments;
  }
  public void setComments(String comments) {
    this.comments = comments;
  }
}
