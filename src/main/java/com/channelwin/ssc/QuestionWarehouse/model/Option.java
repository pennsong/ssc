package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
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
}
