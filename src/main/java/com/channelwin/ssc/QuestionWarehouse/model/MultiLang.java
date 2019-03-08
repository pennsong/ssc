package com.channelwin.ssc.QuestionWarehouse.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Entity
public class MultiLang extends Validatable {
    @Id
    @GeneratedValue
    @Expose
    @Getter
    private int id;

    @Expose
    @Getter
    private String defaultText;

    @ElementCollection
    @Expose
    @Getter
    private Map<Lang, String> translation = new HashMap<>();

    private MultiLang() {
    }

    public MultiLang(String defaultText) {
        this.defaultText = defaultText;
    }

    public void setText(Lang lang, String text) {
        translation.put(lang, text);
    }
}
