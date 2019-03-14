package com.channelwin.ssc.QuestionWarehouse.repository;

import com.channelwin.ssc.QuestionWarehouse.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {
    List<Question> findByTitleDefaultText(String defaultText);

    @Query("SELECT q FROM Question q WHERE q.compoundItem = false")
    Page<Question> findAllTopQuestion(Pageable pageable);
}