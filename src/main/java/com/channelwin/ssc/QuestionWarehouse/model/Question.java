package com.channelwin.ssc.QuestionWarehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Question extends Validatable {
    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private MultiLang title;

    @NotNull
    @Min(0)
    @Setter
    private Double seq;

    @NotNull
    private QuestionType questionType;

    @ManyToOne
    @Setter
    private Category category;

    @NotNull
    private Boolean compoundItem;

    @Setter
    @Size(min = 1, max = 500)
    private String fitRule;

    @NotNull
    @ElementCollection
    private List<ValidateRule> validateRules;

    @ManyToOne
    @JsonIgnore
    private CompoundQuestion compoundQuestion;

    Question(MultiLang title, Double seq, QuestionType questionType, Category category, Boolean compoundItem, String fitRule, List<ValidateRule> validateRules, CompoundQuestion compoundQuestion) {
        this.title = title;
        this.seq = seq;
        this.questionType = questionType;
        this.category = category;
        this.compoundItem = compoundItem;
        this.fitRule = fitRule;

        this.validateRules = new ArrayList<>();

        if (validateRules != null) {
            for (ValidateRule item: validateRules) {
                item.setQuestion(this);
                this.validateRules.add(item);
            }
        }

        this.compoundQuestion = compoundQuestion;
    }

    public void setCompoundQuestion(CompoundQuestion compoundQuestion) {
        if (compoundQuestion == null) {
            throw new RuntimeException("所属复合问题不能为空!");
        }
        this.compoundQuestion = compoundQuestion;
        this.compoundItem = true;
    }

    public String getFitRule() {
        if (StringUtils.isEmpty(this.fitRule)) {
            return "true";
        } else {
            return this.fitRule;
        }
    }

    public String gainTitleDefaultText() {
        return title.getDefaultText();
    }

    public void addValidateRules(List<ValidateRule> validateRules) {
        for (ValidateRule item: validateRules) {
            item.setQuestion(this);
            this.validateRules.add(item);
        }
    }

    @Override
    public void validate() {
        for (ValidateRule item: validateRules) {
            item.validate();
        }
    }

    @PrePersist
    @PreUpdate
    public void prePersist() throws Exception {
        this.validate();
    }
}