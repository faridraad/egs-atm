package com.egs.bank.repository;

import com.egs.bank.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByCardList(Set<String> cardNumber);
}
