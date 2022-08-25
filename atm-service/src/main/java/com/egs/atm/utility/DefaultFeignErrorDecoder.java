package com.egs.atm.utility;

import com.egs.atm.configuration.exception.FeignCustomException;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class DefaultFeignErrorDecoder implements ErrorDecoder {

    public Exception decode(String s, Response response) {
        try {
            return new FeignCustomException(response.status(), response.body() == null ? "" : IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8.toString()));
        } catch (IOException ioException) {
            return new FeignCustomException(0,ioException.getMessage());
        }
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}