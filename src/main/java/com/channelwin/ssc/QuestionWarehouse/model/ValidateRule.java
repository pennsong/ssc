package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateRule {
    private static final String[] singleCandidates = new String[]{
            "一元函数1",
            "一元函数2"
    };

    private static final String[] compoundSingleCandidates = new String[]{
            "复合一元函数1",
            "复合一元函数2"
    };

    private static final String[] compoundTwoCandidates = new String[]{
            "复合二元函数1",
            "复合二元函数2"
    };

    private static final String[] compoundMultiCandidates = new String[]{
            "复合多元函数1",
            "复合多元函数2"
    };

    @NotBlank
    private String name;

    @NotNull
    @ElementCollection
    private List<String> values;

    @NotNull
    private ValidateRuleType validateRuleType;

    public ValidateRule(String validateRule) {
        String singleRulePatternString = "([^()]+)";
        Pattern singleRulePattern = Pattern.compile(singleRulePatternString);

        String compoundRulePatternString = "([^()]+)\\((.*)\\)";
        Pattern compoundRulePattern = Pattern.compile(compoundRulePatternString);

        Matcher singleMatcher = singleRulePattern.matcher(validateRule);

        Matcher compoundMatcher = compoundRulePattern.matcher(validateRule);

        this.values = new ArrayList<>();

        if (singleMatcher.find()) {
            // 一元函数
            this.name = singleMatcher.group(1);
            this.validateRuleType = ValidateRuleType.single;
        } else if (compoundMatcher.find()) {
            // 复合函数
            this.name = compoundMatcher.group(1);
            String valuesString = compoundMatcher.group(2);
            String[] valuesArray = valuesString.split(",");

            // 判断是几元函数
            if (Arrays.stream(compoundSingleCandidates).anyMatch(item -> item.equals(this.name))) {
                this.validateRuleType = ValidateRuleType.compoundSingle;
            } else if (Arrays.stream(compoundTwoCandidates).anyMatch(item -> item.equals(this.name))) {
                this.validateRuleType = ValidateRuleType.compoundTwo;
            } else if (Arrays.stream(compoundMultiCandidates).anyMatch(item -> item.equals(this.name))) {
                this.validateRuleType = ValidateRuleType.compoundMulti;
            } else {
                throw new ValidateException("错误的校验函数名称!");
            }

            // 取得参数list
            for (String item : valuesArray) {
                values.add(item.trim());
            }
        } else {
            throw new ValidateException("校验函数格式不正确!");
        }
    }

    public void validateToQuestion(Question question) {
        if (validateRuleType == ValidateRuleType.single) {
            if (!(question instanceof CompletionQuestion)) {
                throw new ValidateException("'" + name + "'" + " 只能用于填空题!");
            }
        } else {
            if (!(question instanceof CompoundQuestion)) {
                throw new ValidateException("'" + name + "'" + " 只能用于复合题!");
            }

            Set<String> testSet = new HashSet<>();
            testSet.addAll(values);

            if (testSet.size() != values.size()) {
                throw new ValidateException("操作项目不能有重复项!");
            }

            if (validateRuleType == ValidateRuleType.compoundSingle) {
                if (values.size() != 1) {
                    throw new ValidateException("一元函数的参数只能是一位!");
                }
            } else if (validateRuleType == ValidateRuleType.compoundTwo) {
                if (values.size() != 2) {
                    throw new ValidateException("二元函数的参数只能是两位!");
                }
            } else if (validateRuleType == ValidateRuleType.compoundMulti) {
                if (values.size() == 0) {
                    throw new ValidateException("必须要有参数!");
                }
            }

            // values要有对应项目
            CompoundQuestion compoundQuestion = (CompoundQuestion) question;
            Stream<String> titles = compoundQuestion.getQuestions().stream().map(subQuestion -> subQuestion.getTitleDefaultText());

            for (String value: values) {
                if (titles.noneMatch(text -> text.equals(value))) {
                    throw new ValidateException("子问题中没有" + value + "这一项!");
                }
            }
        }
    }
}
