package com.example.demo.dto;

import com.example.demo.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDto {
    private double amount;
    private String currencyCode;
}
