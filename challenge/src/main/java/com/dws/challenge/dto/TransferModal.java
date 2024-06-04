package com.dws.challenge.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferModal {
    private String fromAccountId;

    private String toAccountId;

    private BigDecimal balance;

}
