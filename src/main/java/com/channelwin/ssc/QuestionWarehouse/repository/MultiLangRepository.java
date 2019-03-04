package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.MultiLang;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MultiLangRepository extends CrudRepository<MultiLang, Integer> {
    Collection<MultiLang> findByDefaultText(String defaultText);
}
