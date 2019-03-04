package com.channelwin.ssc.QuestionWarehouse.model;

import javax.persistence.Entity;

@Entity
public class CompletionQuestion extends Question {
    private CompletionQuestion() {
        super(QuestionType.completion);
    }

    public CompletionQuestion(String titleDefaultText) {
        super(QuestionType.completion, titleDefaultText);
    }
}
