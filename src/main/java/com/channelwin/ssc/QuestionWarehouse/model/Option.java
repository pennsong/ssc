package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Option extends Validatable {
    @NotNull
    @Min(0)
    private Integer key;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private MultiLang value;

    @NotNull
    @Setter
    private Integer score;

    Option(Integer key, MultiLang value, Integer score) {
        this.key = key;
        this.value = value;
        this.score = score;
    }
}
