package com.egs.bank.model.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("transaction")
public class Transaction extends BaseEntity {
    private String userId;
    private String type;
    private Long value;
    @Indexed(unique = true)
    private String transactionId;
    @Indexed(unique = true)
    private String rolledBackFor;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRolledBackFor() {
        return rolledBackFor;
    }

    public void setRolledBackFor(String rolledBackFor) {
        this.rolledBackFor = rolledBackFor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
