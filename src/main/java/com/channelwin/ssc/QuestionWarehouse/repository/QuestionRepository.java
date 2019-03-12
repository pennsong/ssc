package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {
}