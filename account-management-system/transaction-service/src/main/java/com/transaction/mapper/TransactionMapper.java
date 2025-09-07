package com.transaction.mapper;

import com.transaction.dto.TransactionRequestDTO;
import com.transaction.dto.TransactionResponseDTO;
import com.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction toEntity(TransactionRequestDTO dto);

    @Mapping(target = "transactionId", source = "transactionId")
    TransactionResponseDTO toResponseDTO(Transaction transaction);
}
