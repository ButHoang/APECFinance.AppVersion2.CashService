import java.math.BigDecimal;

public class AddCashTransactionRequest {
    private BigDecimal tranAmount;

    // Getters and setters
    public BigDecimal getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(BigDecimal tranAmount) {
        this.tranAmount = tranAmount;
    }
}
