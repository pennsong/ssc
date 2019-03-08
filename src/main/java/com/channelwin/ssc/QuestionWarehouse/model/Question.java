package com.channelwin.ssc.QuestionWarehouse.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Question extends Validatable {
    @Id
    @GeneratedValue
    @Expose
    @Getter
    private int id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Expose
    @Getter
    private MultiLang title;

    @Expose
    @Getter
    private int seq;

    @Expose
    @Getter
    private QuestionType questionType;

    @ManyToOne
    @Expose
    @Getter
    private Category category;

    private boolean compoundItem;

    @Getter
    @Setter
    private String fitRule = "true";

    @Expose
    @Getter
    private String validateRule;

    @ManyToOne
    @Setter
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
        this.category = category; this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
    }

    protected Question(QuestionType questionType, String titleDefaultText, Category category, String fitRule) {
        this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.category = category; this.questionType = questionType;
        this.title = new MultiLang(titleDefaultText);
        this.fitRule = fitRule;
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
