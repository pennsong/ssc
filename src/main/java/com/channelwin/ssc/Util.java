package com.channelwin.ssc;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class Util {
    public static <T, ID> T getRecordWithExistCheck(ID id, CrudRepository<T, ID> repository) {
        Optional<T> tOptional = repository.findById(id);

        if (tOptional.isPresent() == false) {
            throw new ValidateException("没有对应此id的项目!");
        }

        return tOptional.get();
    }
}
