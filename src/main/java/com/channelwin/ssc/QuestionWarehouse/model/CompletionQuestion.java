package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletionQuestion extends Question {
    CompletionQuestion(MultiLang title, Double seq, Category category, Boolean compoundItem, String fitRule, List<ValidateRule> validateRules, CompoundQuestion compoundQuestion) {
        super(title, seq, QuestionType.completion, category, compoundItem, fitRule, validateRules, compoundQuestion);
    }
}
