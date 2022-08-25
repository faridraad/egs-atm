package com.egs.bank.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Set;


@Document("user")
public class User extends BaseEntity {
    private String pin;
    private String fingerPrint;
    private Set<String> cardList;
    private Session activeSession;
    private Integer loginAttempt;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public Set<String> getCardList() {
        return cardList;
    }

    public void setCardList(Set<String> cardList) {
        this.cardList = cardList;
    }

    public Session getActiveSession() {
        return activeSession;
    }

    public void setActiveSession(Session activeSession) {
        this.activeSession = activeSession;
    }

    public Integer getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(Integer loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    private static class Session {
        private String token;
        private LocalDateTime createdDate;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public LocalDateTime getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
        }
    }
}
