package com.egs.bank.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Failure {
    private int numberOfAttempts;
    private String message;
}
