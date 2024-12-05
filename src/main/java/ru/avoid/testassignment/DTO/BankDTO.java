package ru.avoid.testassignment.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class BankDTO {
    @Size(min = 2, max = 50, message = "name isn't valid")
    private String bankName;

    @Size(min = 9, max = 9, message = "bankCode isn't valid")
    private String bankCode;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public String toString() {
        return "BankDTO{" +
                "bankName='" + bankName + '\'' +
                '}';
    }
}
