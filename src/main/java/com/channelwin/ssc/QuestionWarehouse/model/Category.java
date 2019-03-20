package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.Validatable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Category extends Validatable {
    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private MultiLang title;

    @Setter
    @NotNull
    @Min(0)
    private Double seq;

    Category(MultiLang title, Double seq) {
        this.title = title;
        this.seq = seq;
    }

    public void setDefaultText(String defaultText) {
        this.title.setDefaultText(defaultText);
    }
}