package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {
    List<Question> findByTitleDefaultText(String defaultText);
}