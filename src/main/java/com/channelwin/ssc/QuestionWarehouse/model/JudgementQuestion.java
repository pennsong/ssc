package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JudgementQuestion extends Question {
    JudgementQuestion(MultiLang title, Double seq, Category category, Boolean compoundItem, String fitRule, List<ValidateRule> validateRules, CompoundQuestion compoundQuestion) {
        super(title, seq, QuestionType.judgement, category, compoundItem, fitRule, validateRules, compoundQuestion);
    }
}
