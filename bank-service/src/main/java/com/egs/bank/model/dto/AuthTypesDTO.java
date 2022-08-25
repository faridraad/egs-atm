package com.egs.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthTypesDTO {
    private List<String> strategies;
}
