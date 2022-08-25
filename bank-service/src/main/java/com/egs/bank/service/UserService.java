package com.egs.bank.service;

import com.egs.bank.configuration.exception.NotAuthenticatedException;
import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.IUser;
import com.egs.bank.model.mapper.UserServiceMapper;
import com.egs.bank.model.domain.UserQuery;
import com.egs.bank.model.entity.BaseEntity;
import com.egs.bank.model.entity.User;
import com.egs.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUser {

    private final UserRepository userRepository;
    private final UserServiceMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationProperties applicationProperties;

    @Override
    public Optional<String> getUserId(UserQuery queryModel) {
        return userRepository.findByCardList(Set.of(queryModel.getCard()))
                .filter(user -> credentialMatches(queryModel, user))
                .map(BaseEntity::getId);
    }

    @Override
    public int addTodayFailedLoginAttempts(String cardNo) {
        User user = mapper.getUserFromUserQueryModel(UserQuery.builder().card(cardNo).build());
        return userRepository.findOne(Example.of(user))
                .map(this::increaseUserTodayAttempts)
                .orElseThrow(() -> new NotAuthenticatedException(applicationProperties.getProperty("error.card.exception.text") + cardNo));

    }

    private Integer increaseUserTodayAttempts(User user) {
        user.setLoginAttempt(increaseAttempts(user.getLoginAttempt()));
        userRepository.save(user);
        return user.getLoginAttempt();
    }

    private Integer increaseAttempts(Integer todayFailedLoginAttempts) {
        return Objects.isNull(todayFailedLoginAttempts) ? 0 : todayFailedLoginAttempts + 1;
    }

    private boolean credentialMatches(UserQuery queryModel, User user) {
        return (Objects.nonNull(queryModel.getPin()) && passwordEncoder.matches(queryModel.getPin(), user.getPin()) ) ||
                (Objects.nonNull(queryModel.getFingerprint()) && passwordEncoder.matches(queryModel.getFingerprint(), user.getFingerPrint()));
    }
}
