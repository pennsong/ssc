package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
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

    ChoiceQuestion(List<Option> options, Boolean multiple, MultiLang title, Double seq, QuestionType questionType, Category category, Boolean compoundItem, String fitRule, List<ValidateRule> validateRules, CompoundQuestion compoundQuestion) {
        super(null, title, seq, questionType, category, compoundItem, fitRule, validateRules, compoundQuestion);
        this.options = options;
        this.multiple = multiple;
    }

    @Override
    public void validate() {
        super.validate();
        if (options.size() < 2) {
            throw new ValidateException("选择题的选项至少要有2项!");
        }
    }
}
