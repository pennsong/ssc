package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.channelwin.ssc.QuestionWarehouse.model.FactoryService.createValidateRule;

@Slf4j
@RunWith(SpringRunner.class)
public class QuestionTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    MultiLang categoryTitle = new MultiLang("目录t1", null);

    MultiLang completionQuestionLabel = new MultiLang("填空题t1", null);
    MultiLang compoundCompletionQuestionLabel = new MultiLang("复合填空题t1", null);

    MultiLang judgementQuestionLabel = new MultiLang("判断题t1", null);
    MultiLang compoundJudgementQuestionLabel = new MultiLang("复合判断题t1", null);

    MultiLang choiceQuestionLabel = new MultiLang("选择题t1", null);
    MultiLang compoundChoiceQuestionLabel = new MultiLang("复合选择题t1", null);

    MultiLang compoundQuestionLabel = new MultiLang("复合题t1", null);

    Category category = new Category(categoryTitle, 1.0);

    //    // 带正确的校验规则
//    填空
//            判断
//    选择(单选, 多选)
//    复合(填空, 判断, 选择单, 选择多)
    @Test
    public void completionQuestion() {
        Throwable exception = assertThrows(ValidateException.class, () -> {
            CompletionQuestion completionQuestion = new CompletionQuestion(completionQuestionLabel,
                    1.0,
                    category,
                    false,
                    null,
                    Arrays.asList(createValidateRule("一元函数1a"), createValidateRule("一元函数2")),
                    null
            );

            Set<ConstraintViolation<CompletionQuestion>> violations = validator.validate(completionQuestion);
        });

        log.info(exception.getMessage());
        assertEquals("错误的校验函数名称!", exception.getMessage());
    }
//
//    // 校验规则
//    不能带校验规则的题目带校验规则:
//    判断 带 一元函数
//    选择 带 一元函数
//
//    错误的函数名:
//    填空 带 NONE函数
//    复合 带 NONE函数(复合填空1)
//    复合 带 NONE函数(复合填空1, 复合判断1)
//    复合 带 NONE函数(复合填空1, 复合判断1, 复合选择1)
//    复合 带 一元函数
//
//    错误的参数个数:
//    填空 带 一元函数(a)
//
//    复合 带 复合一元函数
//    复合 带 复合一元函数()
//    复合 带 复合一元函数(复合填空1, 复合判断1)
//
//    复合 带 复合二元函数
//    复合 带 复合二元函数()
//    复合 带 复合二元函数(复合填空1, 复合判断1, 复合选择1)
//
//    复合 带 复合多元函数
//    复合 带 复合多元函数()
//
//    错误的参数名称:
//    复合 带 复合一元函数(NONE子问题)
//    复合 带 复合二元函数(复合填空1, NONE子问题)
//    复合 带 复合多元函数(NONE子问题)
//
//    重复的参数名称:
//    复合 带 复合二元函数(复合填空1, 复合填空1)
//    复合 带 复合多元函数(复合填空1, 复合填空1)
}
