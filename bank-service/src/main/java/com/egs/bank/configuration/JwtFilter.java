package com.egs.bank.configuration;


import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.ITokenGranter;
import com.egs.bank.model.dto.ErrorDTO;
import com.egs.bank.utility.ConstantParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Order
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private ITokenGranter tokenGranter;
    private final ApplicationProperties applicationProperties;

    public JwtFilter(ITokenGranter tokenGranter  , ApplicationProperties applicationProperties ) {

        this.tokenGranter = tokenGranter;
        this.applicationProperties = applicationProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader(ConstantParam.Authorization);
        getRejectionMessage(authHeader).ifPresentOrElse(
                message -> returnProperUnAuthorizedResponse(httpServletResponse, message),
                () -> continueWithRequest(httpServletRequest, httpServletResponse, filterChain)
        );


    }

    private void continueWithRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (IOException e) {
            log.error(applicationProperties.getProperty("error.jwt.filter.io.exception.text"), e);
        } catch (ServletException e) {
            log.error(applicationProperties.getProperty("error.jwt.filter.servlet.exception.text"), e);
        }
    }

    private void returnProperUnAuthorizedResponse(HttpServletResponse httpServletResponse, String message) {
        try {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().print(
                    new ObjectMapper().writer().writeValueAsString(ErrorDTO.builder().message(message).build())
            );
            httpServletResponse.getWriter().flush();
        } catch (IOException e) {
            log.error(applicationProperties.getProperty("error.jwt.filter.io.exception.text"), e);
        }
    }

    private Optional<String> getRejectionMessage(String authHeader) {
        if(Objects.isNull(authHeader) || !authHeader.contains(ConstantParam.Bearer))
            return Optional.of(applicationProperties.getProperty("message.provide.token.text"));
        authHeader = authHeader.replace(ConstantParam.Bearer, "");
        return getProperMessageBasedOnJwtException(authHeader);


    }

    private Optional<String> getProperMessageBasedOnJwtException(String token) {
        try {
            tokenGranter.getUserIdFromToken(token);
            return Optional.empty();
        } catch (ExpiredJwtException exception) {
            log.warn(applicationProperties.getProperty("log.warn.jwt.parse.expired.exception.text"), token, exception.getMessage());
            return Optional.of(applicationProperties.getProperty("optional.jwt.expire"));
        } catch (UnsupportedJwtException exception) {
            log.warn(applicationProperties.getProperty("log.warn.jwt.parse.unsupported.exception.text"), token, exception.getMessage());
            return Optional.of(applicationProperties.getProperty("optional.jwt.unsupported"));
        } catch (MalformedJwtException exception) {
            log.warn(applicationProperties.getProperty("log.warn.jwt.parse.invalid.exception.text"), token, exception.getMessage());
            return Optional.of(applicationProperties.getProperty("optional.jwt.malformed"));
        } catch (Exception exception){
            log.warn(applicationProperties.getProperty("log.warn.jwt.parse.invalid.exception.text"), token, exception.getMessage());
            return Optional.of(applicationProperties.getProperty("optional.jwt.invalid"));
        }
    }
}
