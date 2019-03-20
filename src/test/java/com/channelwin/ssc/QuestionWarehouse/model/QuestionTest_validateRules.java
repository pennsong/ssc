package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

import static com.channelwin.ssc.QuestionWarehouse.model.FactoryService.createValidateRule;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@RunWith(SpringRunner.class)
//@SpringBootTest
public class QuestionTest_validateRules {
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
        log.info("before");
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

    @After
    public void after() {
        log.info("after");
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

        completionQuestion.validate();
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

        judgementQuestion.validate();
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

        choiceQuestion.validate();
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

        choiceQuestion.validate();
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

        compoundQuestion.validate();
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
            judgementQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("只能用于填空题"));
    }

    @Test
    public void 选择题单创建_失败_带校验规则() {
        Exception exception = assertThrows(ValidateException.class, () -> {
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

            choiceQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("只能用于填空题"));
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
            completionQuestion.validate();
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
            compoundQuestion.validate();
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
            compoundQuestion.validate();
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
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 一元函数
    @Test
    public void 复合题创建_失败_带一元函数() {
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
                            createValidateRule("一元函数1")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("只能用于填空题"));
    }
    // end 错误的函数名

    // 错误的参数个数

    // 填空 带 一元函数(a)
    @Test
    public void 填空题创建_失败_带_一元函数() {
        Exception exception = assertThrows(ValidateException.class, () -> {
            CompletionQuestion completionQuestion = new CompletionQuestion(completionQuestionLabel,
                    1.0,
                    category,
                    false,
                    null,
                    Arrays.asList(createValidateRule("一元函数1(a)")),
                    null
            );
            completionQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 复合一元函数
    @Test
    public void 复合题创建_失败_带_复合一元函数_1() {
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
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 复合一元函数()
    @Test
    public void 复合题创建_失败_带_复合一元函数_2() {
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
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("校验函数格式不正确"));
    }

    // 复合 带 复合一元函数(复合填空1, 复合判断1)
    @Test
    public void 复合题创建_失败_带_复合一元函数_3() {
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
                            createValidateRule("复合一元函数1(复合填空1, 复合判断1)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("一元函数的参数只能是一位"));
    }

    // 复合 带 复合二元函数
    @Test
    public void 复合题创建_失败_带_复合二元函数_1() {
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
                            createValidateRule("复合二元函数1")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 复合二元函数()
    @Test
    public void 复合题创建_失败_带_复合二元函数_2() {
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
                            createValidateRule("复合二元函数1()")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("校验函数格式不正确"));
    }

    // 复合 带 复合二元函数(复合填空1, 复合判断1, 复合选择1)
    @Test
    public void 复合题创建_失败_带_复合二元函数_3() {
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
                            createValidateRule("复合二元函数1(复合填空1, 复合判断1, 复合选择1)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("二元函数的参数只能是两位"));
    }

    // 复合 带 复合多元函数
    @Test
    public void 复合题创建_失败_带_复合多元函数_1() {
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
                            createValidateRule("复合多元函数1")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("错误的校验函数名称"));
    }

    // 复合 带 复合多元函数()
    @Test
    public void 复合题创建_失败_带_复合多元函数_2() {
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
                            createValidateRule("复合多元函数1()")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("校验函数格式不正确"));
    }

    // end 错误的参数个数


    // 错误的参数名称
    // 复合 带 复合一元函数(NONE子问题)
    @Test
    public void 复合题创建_失败_带_复合一元函数_错误参数_1() {
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
                            createValidateRule("复合一元函数1(NONE子问题)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("子问题中没有"));
    }

    // 复合 带 复合二元函数(复合填空1, NONE子问题)
    @Test
    public void 复合题创建_失败_带_复合二元函数_错误参数() {
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
                            createValidateRule("复合二元函数1(复合填空1, NONE子问题)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("子问题中没有"));
    }

    // 复合 带 复合多元函数(NONE子问题)
    @Test
    public void 复合题创建_失败_带_复合多元函数_错误参数() {
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
                            createValidateRule("复合多元函数1(NONE子问题)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("子问题中没有"));
    }
    // end 错误的参数名称

    // 重复的参数名称
    // 复合 带 复合二元函数(复合填空1, 复合填空1)
    @Test
    public void 复合题创建_失败_带_复合二元函数_重复的参数名称() {
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
                            createValidateRule("复合二元函数1(复合填空1, 复合填空1)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("操作项目不能有重复项"));
    }

    // 复合 带 复合多元函数(复合填空1, 复合填空1)
    @Test
    public void 复合题创建_失败_带_复合多元函数_重复的参数名称() {
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
                            createValidateRule("复合多元函数1(复合填空1, 复合填空1)")
                    )
            );
            compoundQuestion.validate();
        });

        assertEquals(true, exception.getMessage().contains("操作项目不能有重复项"));
    }
    // end 重复的参数名称
}