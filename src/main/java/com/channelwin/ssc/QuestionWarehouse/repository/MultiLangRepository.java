package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.MultiLang;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MultiLangRepository extends CrudRepository<MultiLang, Integer> {
    List<MultiLang> findByDefaultText(String defaultText);
}
