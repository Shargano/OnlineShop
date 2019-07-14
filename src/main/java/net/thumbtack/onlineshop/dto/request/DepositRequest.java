package net.thumbtack.onlineshop.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class DepositRequest {
    @NotNull(message = "Deposit can't be null")
    @Positive(message = "Deposit must be positive")
    private int deposit;

    public DepositRequest() {
    }

    public DepositRequest(int deposit) {
        this.deposit = deposit;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}