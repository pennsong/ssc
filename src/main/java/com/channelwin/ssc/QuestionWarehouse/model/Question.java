package com.channelwin.ssc.QuestionWarehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Question extends Validatable {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private MultiLang title;

    private double seq;

    private QuestionType questionType;

    @ManyToOne
    private Category category;

    private boolean compoundItem;

    @Setter
    @Getter
    private String fitRule = "true";

    private String validateRule;

    @ManyToOne
    @JsonIgnore
    private CompoundQuestion compoundQuestion;

    protected Question() {
    }

    protected Question(QuestionType questionType) {
        this.questionType = questionType;
    }

    protected Question(QuestionType questionType, String titleDefaultText) {
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
    }

    protected Question(QuestionType questionType, String titleDefaultText, Category category) {
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.category = category;
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
    }

    protected Question(QuestionType questionType, String titleDefaultText, Category category, String fitRule) {
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.category = category;
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.fitRule = fitRule;
    }

    protected Question(QuestionType questionType, String titleDefaultText, Category category, String fitRule, String validateRule) {
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.category = category;
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.fitRule = fitRule;
        this.validateRule = validateRule;
    }

    public void setCompoundQuestion(CompoundQuestion compoundQuestion) {
        if (compoundQuestion == null) {
            throw new RuntimeException("所属复合问题不能为空!");
        }
        this.compoundQuestion = compoundQuestion;
        this.compoundItem = true;
    }

    @PrePersist
    @PreUpdate
    public void prePersist() throws Exception {
        this.validate();
    }
}
