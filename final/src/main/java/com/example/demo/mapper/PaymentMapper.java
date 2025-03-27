package com.example.demo.mapper;

import com.example.demo.dto.DepositDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    @Mapping(target = "paymentType", source = "paymentType")
    @Mapping(target = "status", source = "status")
    PaymentDto toDto(Payment payment);
    List<PaymentDto> toDtoList(List<Payment> payments);

    @Mapping(target = "amount",source = "amount")
    @Mapping(target = "currencyCode",source = "currencyCode")
    DepositDto toDepositDto(Double amount,String currencyCode);

}
