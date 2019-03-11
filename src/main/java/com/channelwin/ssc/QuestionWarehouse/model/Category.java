package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.Setter;

import javax.persistence.*;

@Entity
public class Category extends Validatable {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private MultiLang title;

    @Setter
    private float seq;

    private Category() {
    }

    public Category(String titleDefaultText, float seq){
        this.title = new MultiLang(titleDefaultText);
        this.seq = seq;
    }

    public Category(String titleDefaultText, float seq, String... langs){
        this.title = new MultiLang(titleDefaultText);
        this.seq = seq;
        for (String item: langs) {
            String[] strings = item.split(":");
            this.title.setText(Lang.valueOf(strings[0]), strings[1]);
        }
    }

    public void setDefaultText(String defaultText) {
        this.title.setDefaultText(defaultText);
    }

    @PrePersist
    public void prePersist() {
        this.validate();
    }
}
