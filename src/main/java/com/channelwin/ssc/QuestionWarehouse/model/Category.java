package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.validate();
    }
}