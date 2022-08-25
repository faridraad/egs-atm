package com.egs.bank.utility;

public class AuthUtil {
    AuthUtil(){
        
    }
    public static String getBearerToken(String authorizationHeader) {
        return authorizationHeader.replace(ConstantParam.Bearer, "");
    }
}
