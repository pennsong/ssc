package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.Getter;
import lombok.Setter;

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
    private int id;

    @Getter
    @Setter
    private String defaultText;

    @ElementCollection
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
