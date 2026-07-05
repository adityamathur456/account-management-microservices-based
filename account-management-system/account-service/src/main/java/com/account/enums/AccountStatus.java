package com.account.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED,
    CLOSED;

    @JsonCreator
    public static AccountStatus fromString(String value) {
        return AccountStatus.valueOf(value.toUpperCase());
    }
}
