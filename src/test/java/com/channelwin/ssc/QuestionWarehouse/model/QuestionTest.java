package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.channelwin.ssc.QuestionWarehouse.model.FactoryService.createValidateRule;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@RunWith(SpringRunner.class)
public class QuestionTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    MultiLang categoryTitle;

    MultiLang completionQuestionLabel;
    MultiLang compoundCompletionQuestionLabel;

    MultiLang judgementQuestionLabel;
    MultiLang compoundJudgementQuestionLabel;

    MultiLang choiceQuestionLabel;
    MultiLang compoundChoiceQuestionLabel;

    MultiLang compoundQuestionLabel;

    Category category;

    Option option1;
    Option option2;
    List<Option> optionList;

    CompletionQuestion compoundCompletionQuestion;
    JudgementQuestion compoundJudgementQuestion;
    ChoiceQuestion compoundChoiceQuestion;

    List<Question> questionList;

    CompoundQuestion compoundQuestion;


    @Before
    public void Before() {
        categoryTitle = new MultiLang("目录t1", null);

        completionQuestionLabel = new MultiLang("填空题t1", null);
        compoundCompletionQuestionLabel = new MultiLang("复合填空题t1", null);

        judgementQuestionLabel = new MultiLang("判断题t1", null);
        compoundJudgementQuestionLabel = new MultiLang("复合判断题t1", null);

        choiceQuestionLabel = new MultiLang("选择题t1", null);
        compoundChoiceQuestionLabel = new MultiLang("复合选择题t1", null);

        compoundQuestionLabel = new MultiLang("复合题t1", null);

        category = new Category(categoryTitle, 1.0);

        option1 = new Option(0, new MultiLang("选项1", null), 10);
        option2 = new Option(0, new MultiLang("选项2", null), 20);
        optionList = Arrays.asList(option1, option2);

        compoundCompletionQuestion = new CompletionQuestion(compoundCompletionQuestionLabel,
                1.0,
                null,
                true,
                null,
                Arrays.asList(createValidateRule("一元函数1"), createValidateRule("一元函数2")),
                null
        );

        compoundJudgementQuestion = new JudgementQuestion(compoundJudgementQuestionLabel,
                1.0,
                null,
                true,
                null,
                null,
                null
        );

        compoundChoiceQuestion = new ChoiceQuestion(
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

        questionList = Arrays.asList(compoundCompletionQuestion, compoundJudgementQuestion, compoundChoiceQuestion);
    }

    // 成功创建问题
    @Test
    public void 填空题创建() {
        CompletionQuestion completionQuestion = new CompletionQuestion(completionQuestionLabel,
                1.0,
                category,
                false,
                null,
                Arrays.asList(createValidateRule("一元函数1"), createValidateRule("一元函数2")),
                null
        );

        Set<ConstraintViolation<CompletionQuestion>> violations = validator.validate(completionQuestion);
        assertEquals(0, violations.size());
    }

    @Test
    public void 判断题创建() {
        JudgementQuestion judgementQuestion = new JudgementQuestion(judgementQuestionLabel,
                1.0,
                category,
                false,
                null,
                null,
                null
        );

        Set<ConstraintViolation<JudgementQuestion>> violations = validator.validate(judgementQuestion);
        assertEquals(0, violations.size());
    }

    @Test
    public void 选择题单创建() {
        ChoiceQuestion choiceQuestion = new ChoiceQuestion(
                optionList,
                false,
                choiceQuestionLabel,
                1.0,
                category,
                false,
                null,
                null,
                null
        );

        Set<ConstraintViolation<ChoiceQuestion>> violations = validator.validate(choiceQuestion);
        assertEquals(0, violations.size());
    }

    @Test
    public void 选择题多创建() {
        ChoiceQuestion choiceQuestion = new ChoiceQuestion(
                optionList,
                true,
                choiceQuestionLabel,
                1.0,
                category,
                false,
                null,
                null,
                null
        );

        Set<ConstraintViolation<ChoiceQuestion>> violations = validator.validate(choiceQuestion);
        assertEquals(0, violations.size());
    }

    @Test
    public void 复合题创建() {
        CompoundQuestion compoundQuestion = new CompoundQuestion(
                1,
                2,
                questionList,
                compoundQuestionLabel,
                1.0,
                category,
                null,
                Arrays.asList(
                        createValidateRule("复合一元函数1(复合填空题t1)"),
                        createValidateRule("复合二元函数1(复合填空题t1, 复合判断题t1)"),
                        createValidateRule("复合多元函数1(复合填空题t1, 复合判断题t1, 复合选择题t1)")
                )
        );

        Set<ConstraintViolation<CompoundQuestion>> violations = validator.validate(compoundQuestion);
        assertEquals(0, violations.size());
    }
    // end 成功创建问题

    // 不能带校验规则的题目带校验规则
    @Test
    public void 判断题创建_失败_带校验规则() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            JudgementQuestion judgementQuestion = new JudgementQuestion(judgementQuestionLabel,
                    1.0,
                    null,
                    false,
                    null,
                    Arrays.asList(createValidateRule("一元函数1"), createValidateRule("一元函数2")),
                    null
            );
            Set<ConstraintViolation<JudgementQuestion>> violations = validator.validate(judgementQuestion);
        });

        assertEquals(true, exception.getMessage().contains("只能用于填空题"));
    }

    @Test
    public void 选择题单创建_失败_带校验规则() {
        ChoiceQuestion choiceQuestion = new ChoiceQuestion(
                optionList,
                false,
                choiceQuestionLabel,
                1.0,
                category,
                false,
                null,
                Arrays.asList(createValidateRule("一元函数1"), createValidateRule("一元函数2")),
                null
        );

        Set<ConstraintViolation<ChoiceQuestion>> violations = validator.validate(choiceQuestion);
        assertEquals(true, violations.stream().findFirst().get().getMessage().contains("只能用于填空题"));
    }
    // end 不能带校验规则的题目带校验规则

    // 错误的函数名
    @Test
    public void 填空题创建_失败_带NONE函数() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompletionQuestion completionQuestion = new CompletionQuestion(completionQuestionLabel,
                    1.0,
                    category,
                    false,
                    null,
                    Arrays.asList(createValidateRule("NONE函数")),
                    null
            );
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 NONE函数(复合填空1)
    @Test
    public void 复合题创建_失败_带NONE函数_1() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompoundQuestion compoundQuestion = new CompoundQuestion(
                    1,
                    2,
                    questionList,
                    compoundQuestionLabel,
                    1.0,
                    category,
                    null,
                    Arrays.asList(
                            createValidateRule("NONE函数(复合填空题t1)")
                    )
            );
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 NONE函数(复合填空1, 复合判断1)
    @Test
    public void 复合题创建_失败_带NONE函数_2() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompoundQuestion compoundQuestion = new CompoundQuestion(
                    1,
                    2,
                    questionList,
                    compoundQuestionLabel,
                    1.0,
                    category,
                    null,
                    Arrays.asList(
                            createValidateRule("NONE函数(复合填空题t1, 复合判断1)")
                    )
            );
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 NONE函数(复合填空1, 复合判断1, 复合选择1)
    @Test
    public void 复合题创建_失败_带NONE函数_3() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompoundQuestion compoundQuestion = new CompoundQuestion(
                    1,
                    2,
                    questionList,
                    compoundQuestionLabel,
                    1.0,
                    category,
                    null,
                    Arrays.asList(
                            createValidateRule("NONE函数(复合填空题t1, 复合判断1, 复合选择1)")
                    )
            );
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 一元函数
    @Test
    public void 复合题创建_失败_带一元函数() {
        CompoundQuestion compoundQuestion = new CompoundQuestion(
                1,
                2,
                questionList,
                compoundQuestionLabel,
                1.0,
                category,
                null,
                Arrays.asList(
                        createValidateRule("一元函数1")
                )
        );

        Set<ConstraintViolation<CompoundQuestion>> violations = validator.validate(compoundQuestion);
        assertEquals(true, violations.stream().findFirst().get().getMessage().contains("只能用于填空题"));
    }
    // end 错误的函数名

    // 错误的参数个数

    // 填空 带 一元函数(a)
    @Test
    public void 填空题创建_带_一元函数() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompletionQuestion completionQuestion = new CompletionQuestion(completionQuestionLabel,
                    1.0,
                    category,
                    false,
                    null,
                    Arrays.asList(createValidateRule("一元函数1(a)")),
                    null
            );
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 复合一元函数
    @Test
    public void 复合题创建_失败_复合一元函数_1() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompoundQuestion compoundQuestion = new CompoundQuestion(
                    1,
                    2,
                    questionList,
                    compoundQuestionLabel,
                    1.0,
                    category,
                    null,
                    Arrays.asList(
                            createValidateRule("复合一元函数1")
                    )
            );
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 复合一元函数()
    @Test
    public void 复合题创建_失败_复合一元函数_2() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompoundQuestion compoundQuestion = new CompoundQuestion(
                    1,
                    2,
                    questionList,
                    compoundQuestionLabel,
                    1.0,
                    category,
                    null,
                    Arrays.asList(
                            createValidateRule("复合一元函数1()")
                    )
            );
        });

        assertEquals(true, exception.getMessage().contains("校验函数格式不正确"));
    }

    // 复合 带 复合一元函数(复合填空1, 复合判断1)
    @Test
    public void 复合题创建_失败_复合一元函数_3() {
        CompoundQuestion compoundQuestion = new CompoundQuestion(
                1,
                2,
                questionList,
                compoundQuestionLabel,
                1.0,
                category,
                null,
                Arrays.asList(
                        createValidateRule("复合一元函数1(复合填空1, 复合判断1)")
                )
        );

        Set<ConstraintViolation<CompoundQuestion>> violations = validator.validate(compoundQuestion);
        assertEquals(true, violations.stream().findFirst().get().getMessage().contains("一元函数的参数只能是一位"));
    }

    // 复合 带 复合二元函数
//    @Test
//    public void 复合题创建_失败_复合二元函数_1() {
//        CompoundQuestion compoundQuestion = new CompoundQuestion(
//                1,
//                2,
//                questionList,
//                compoundQuestionLabel,
//                1.0,
//                category,
//                null,
//                Arrays.asList(
//                        createValidateRule("复合二元函数1")
//                )
//        );
//
//        Set<ConstraintViolation<CompoundQuestion>> violations = validator.validate(compoundQuestion);
//        assertEquals(true, violations.stream().findFirst().get().getMessage().contains("一元函数的参数只能是一位"));
//    }

    // 复合 带 复合二元函数()

    // 复合 带 复合二元函数(复合填空1, 复合判断1, 复合选择1)

    // 复合 带 复合多元函数

    // 复合 带 复合多元函数()

    // end 错误的参数个数


//    错误的参数名称:
//    复合 带 复合一元函数(NONE子问题)
//    复合 带 复合二元函数(复合填空1, NONE子问题)
//    复合 带 复合多元函数(NONE子问题)
//
//    重复的参数名称:
//    复合 带 复合二元函数(复合填空1, 复合填空1)
//    复合 带 复合多元函数(复合填空1, 复合填空1)

}