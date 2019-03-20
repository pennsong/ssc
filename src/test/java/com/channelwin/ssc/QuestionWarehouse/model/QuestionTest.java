package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

import static com.channelwin.ssc.QuestionWarehouse.model.FactoryService.createValidateRule;

@Slf4j
@RunWith(SpringRunner.class)
public class QuestionTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    CompletionQuestion completionQuestion;
    JudgementQuestion judgementQuestion;
    ChoiceQuestion choiceQuestion;
    CompoundQuestion compoundQuestion;

    @Before
    public void Before() {
        MultiLang categoryTitle = new MultiLang("目录t1", null);

        MultiLang completionQuestionLabel = new MultiLang("填空题t1", null);
        MultiLang compoundCompletionQuestionLabel = new MultiLang("复合填空题t1", null);

        MultiLang judgementQuestionLabel = new MultiLang("判断题t1", null);
        MultiLang compoundJudgementQuestionLabel = new MultiLang("复合判断题t1", null);

        MultiLang choiceQuestionLabel = new MultiLang("选择题t1", null);
        MultiLang compoundChoiceQuestionLabel = new MultiLang("复合选择题t1", null);

        MultiLang compoundQuestionLabel = new MultiLang("复合题t1", null);

        Category category = new Category(categoryTitle, 1.0);

        Option option1 = new Option(0, new MultiLang("选项1", null), 10);
        Option option2 = new Option(0, new MultiLang("选项2", null), 20);
        List<Option> optionList = Arrays.asList(option1, option2);

        completionQuestion = new CompletionQuestion(compoundCompletionQuestionLabel,
                1.0,
                category,
                false,
                null,
                Arrays.asList(createValidateRule("一元函数1"), createValidateRule("一元函数2")),
                null
        );

        judgementQuestion = new JudgementQuestion(compoundJudgementQuestionLabel,
                1.0,
                category,
                false,
                null,
                null,
                null
        );

        choiceQuestion = new ChoiceQuestion(
                optionList,
                false,
                compoundChoiceQuestionLabel,
                1.0,
                category,
                false,
                null,
                null,
                null
        );

        CompletionQuestion compoundCompletionQuestion = new CompletionQuestion(compoundCompletionQuestionLabel,
                1.0,
                null,
                true,
                null,
                Arrays.asList(createValidateRule("一元函数1"), createValidateRule("一元函数2")),
                null
        );

        JudgementQuestion compoundJudgementQuestion = new JudgementQuestion(compoundJudgementQuestionLabel,
                1.0,
                null,
                true,
                null,
                null,
                null
        );

        ChoiceQuestion compoundChoiceQuestion = new ChoiceQuestion(
                optionList,
                false,
                compoundChoiceQuestionLabel,
                1.0,
                null,
                true,
                null,
                null,
                null
        );

        List<Question> questionList = Arrays.asList(compoundCompletionQuestion, compoundJudgementQuestion, compoundChoiceQuestion);

        compoundQuestion = new CompoundQuestion(1,
                2,
                questionList,
                compoundQuestionLabel,
                1.1, category,
                null,
                null);
    }

    @After
    public void after() {
    }
}
