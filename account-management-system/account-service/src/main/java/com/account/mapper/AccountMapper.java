package com.account.mapper;

import com.account.dto.AccountDTO;
import com.account.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);
    Account toEntity(AccountDTO dto);
}
