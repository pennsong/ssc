package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChoiceQuestion extends Question {
    @ElementCollection
    @NotNull
    private List<Option> options;

    @NotNull
    private Boolean multiple;

    ChoiceQuestion(List<Option> options, Boolean multiple, MultiLang title, Double seq, QuestionType questionType, Category category, Boolean compoundItem, String fitRule, String validateRule, CompoundQuestion compoundQuestion) {
        super(null, title, seq, questionType, category, compoundItem, fitRule, validateRule, compoundQuestion);
        this.options = options;
        this.multiple = multiple;
    }
}
