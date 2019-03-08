package com.channelwin.ssc.QuestionWarehouse.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChoiceQuestion extends Question {
    @ElementCollection
    @Expose
    @Getter
    private List<Option> options = new ArrayList();

    @Expose
    @Getter
    private boolean multiple;

    private ChoiceQuestion() {
        super(QuestionType.choice);
    }

    public ChoiceQuestion(String titleDefaultText, String... optionDefaultTexts) {
        super(QuestionType.choice, titleDefaultText);
        for (int i = 0; i < optionDefaultTexts.length; i++) {
            this.options.add(new Option(i, optionDefaultTexts[i]));
        }
    }
}
