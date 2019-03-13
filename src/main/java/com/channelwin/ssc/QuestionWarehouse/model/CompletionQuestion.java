package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletionQuestion extends Question {
    CompletionQuestion(MultiLang title, Double seq, QuestionType questionType, Category category, Boolean compoundItem, String fitRule, String validateRule, CompoundQuestion compoundQuestion) {
        super(null, title, seq, questionType, category, compoundItem, fitRule, validateRule, compoundQuestion);
    }
}
