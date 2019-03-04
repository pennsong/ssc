package com.channelwin.ssc.QuestionWarehouse.model;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CompoundQuestion extends Question {
    @Getter
    @Setter
    @Expose
    private int minNum = 1;

    @Setter
    @Expose
    private int maxNum = 1;

    @OneToMany(mappedBy = "compoundQuestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Expose
    private List<Question> questions = new ArrayList<>();

    private CompoundQuestion() {
        super(QuestionType.compound);
    }

    public CompoundQuestion(String titleDefaultText, Category category, Question... questions) {
        super(QuestionType.compound, titleDefaultText, category);
        for (Question item: questions) {
            if (item instanceof CompoundQuestion) {
                throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "不能嵌套复合项目!");
            }
            this.questions.add(item);
            item.setCompoundQuestion(this);
        }
    }

    @Override
    public void validate() {
        super.validate();
        if (maxNum < minNum) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "maxNum不能小于minNum!");
        }
    }
}
