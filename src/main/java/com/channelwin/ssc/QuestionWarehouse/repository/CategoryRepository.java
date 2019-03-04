package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.Category;
import com.channelwin.ssc.QuestionWarehouse.model.MultiLang;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findByTitleDefaultText(String defaultText);
}
