package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CompoundQuestion extends Question {
    @Getter
    @Setter
    @Expose
    private int minNum = 1;

    @Getter
    @Setter
    @Expose
    private int maxNum = 1;

    @OneToMany(mappedBy = "compoundQuestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Getter
    @Expose
    private List<Question> questions = new ArrayList<>();

    private CompoundQuestion() {
        super(QuestionType.compound);
    }

    public CompoundQuestion(String titleDefaultText, Category category, Question... questions) {
        super(QuestionType.compound, titleDefaultText, category);
        for (Question item : questions) {
            if (item instanceof CompoundQuestion) {
                throw new ValidateException("不能嵌套复合项目!");
            }
            this.questions.add(item);
            item.setCompoundQuestion(this);
        }
    }

    public CompoundQuestion(String titleDefaultText, Category category, String fitRule, Question... questions) {
        this(titleDefaultText, category, questions);
        setFitRule(fitRule);
    }

    @Override
    public void validate() {
        super.validate();
        if (maxNum < minNum) {
            throw new ValidateException("maxNum不能小于minNum!");
        }
    }
}
