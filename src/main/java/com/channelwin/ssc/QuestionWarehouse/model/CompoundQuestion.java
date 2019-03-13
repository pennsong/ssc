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
    private Integer minNum = 1;

    @Getter
    @Setter
    @NotNull
    private Integer maxNum = 1;

    @OneToMany(mappedBy = "compoundQuestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private List<Question> questions;

    CompoundQuestion(Integer minNum, Integer maxNum, List<Question> questions, MultiLang title, Double seq, QuestionType questionType, Category category, String fitRule, String validateRule) {
        super(null, title, seq, questionType, category, false, fitRule, validateRule, null);
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
    }
}
