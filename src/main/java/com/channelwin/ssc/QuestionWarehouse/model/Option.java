package com.channelwin.ssc.QuestionWarehouse.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class Option extends Validatable {
    @Expose
    @Getter
    private int key;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Expose
    @Getter
    private MultiLang value;

    @Expose
    @Getter
    private int score;

    private Option() {
    }

    public Option(int key, String valueDefaultText) {
        this.key = key;
        this.score = 0;
        this.value = new MultiLang(valueDefaultText);
    }
}
