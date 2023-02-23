package com.wojto.storage.mappers;

import com.wojto.model.User;
import com.wojto.model.UserImpl;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class UserMapper implements FieldSetMapper<User> {

    @Override
    public User mapFieldSet(FieldSet fieldSet) throws BindException {
        User user = new UserImpl();
        user.setId(fieldSet.readLong("id"));
        user.setName(fieldSet.readString("name"));
        user.setEmail(fieldSet.readString("email"));
        return user;
    }
}
