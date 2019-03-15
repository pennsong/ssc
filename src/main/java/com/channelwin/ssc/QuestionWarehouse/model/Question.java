package com.channelwin.ssc.QuestionWarehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
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

    public String getTitleDefaultText2() {
        return title.getDefaultText();
    }

    public void addValidateRules(List<ValidateRule> validateRules) {
        this.validateRules.addAll(validateRules);
    }

    @Override
    public void validate() {
        for (ValidateRule item: validateRules) {
            item.validateToQuestion(this);
        }
    }

    @PrePersist
    @PreUpdate
    public void prePersist() throws Exception {
        this.validate();
    }
}