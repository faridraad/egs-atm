package com.egs.bank.model.mapper;

import com.egs.bank.model.entity.User;
import com.egs.bank.model.domain.UserQuery;
import org.mapstruct.*;

import java.util.Set;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring",
        imports = Set.class
)
public interface UserServiceMapper {

    @Mapping(target = "cardList", expression = "java((queryModel.getCard() == null) ? null : Set.of(queryModel.getCard()))")
    User getUserFromUserQueryModel(UserQuery queryModel);
}
