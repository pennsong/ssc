package com.channelwin.ssc.QuestionWarehouse.model;

import javax.persistence.Entity;

@Entity
public class JudgementQuestion extends Question {
    private JudgementQuestion() {
        super(QuestionType.judgement);
    }

    public JudgementQuestion(String titleDefaultText) {
        super(QuestionType.judgement, titleDefaultText);
    }
}
