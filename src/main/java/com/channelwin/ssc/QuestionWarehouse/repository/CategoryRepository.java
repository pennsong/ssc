package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    List<Category> findByTitleDefaultText(String defaultText);
}
