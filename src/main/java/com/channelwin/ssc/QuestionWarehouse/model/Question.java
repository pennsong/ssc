package com.channelwin.ssc.QuestionWarehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Question extends Validatable {
    @Id
    @GeneratedValue
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
    @Getter
    @Size(min = 1, max = 500)
    private String fitRule;

    @Setter
    @Size(min = 1, max = 500)
    private String validateRule;

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

    @PrePersist
    @PreUpdate
    public void prePersist() throws Exception {
        this.validate();
    }
}
