package com.egs.bank.intrface;

import com.egs.bank.model.domain.UserQuery;

import java.util.Optional;

public interface IUser {

    Optional<String> getUserId(UserQuery queryModel);
    /**
     *
     * @param cardNo
     * @return number of today attempts
     */
    int addTodayFailedLoginAttempts(String cardNo);
}
