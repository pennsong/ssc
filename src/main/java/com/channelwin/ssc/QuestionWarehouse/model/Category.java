package com.channelwin.ssc.QuestionWarehouse.model;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Map;

@Entity
public class Category extends Validatable {
    @Id
    @GeneratedValue
    @Expose
    private int id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Expose
    private MultiLang title;

    @Expose
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

    @PrePersist
    public void prePersist() {
        this.validate();
    }
}
