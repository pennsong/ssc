package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompoundQuestion extends Question {
    @Getter
    @Setter
    @NotNull
    private Integer minNum;

    @Getter
    @Setter
    @NotNull
    private Integer maxNum;

    @OneToMany(mappedBy = "compoundQuestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    @Getter
    private List<Question> questions;

    CompoundQuestion(Integer minNum, Integer maxNum, List<Question> questions, MultiLang title, Double seq, QuestionType questionType, Category category, String fitRule, List<ValidateRule> validateRules) {
        super(null, title, seq, questionType, category, false, fitRule, validateRules, null);
        this.minNum = minNum;
        this.maxNum = maxNum;

        this.questions = new ArrayList<>();

        for (Question item: questions) {
            if (item instanceof CompoundQuestion) {
                throw new ValidateException("子问题不能是复合题!");
            }
            // 子问题从属于复合题的目录
            item.setCategory(null);
            item.setCompoundQuestion(this);
            // 子问题没有单独的fitRule
            item.setFitRule(null);
            this.questions.add(item);
        }
    }

    @Override
    public void validate() {
        super.validate();
        if (maxNum < minNum) {
            throw new ValidateException("maxNum不能小于minNum!");
        }

        if (questions.size() < 1) {
            throw new ValidateException("复合题的子问题至少要有1项!");
        }

        for (Question item : questions) {
            if (item instanceof CompoundQuestion) {
                throw new ValidateException("子问题不能是复合题!");
            }
            item.validate();
        }
    }
}
