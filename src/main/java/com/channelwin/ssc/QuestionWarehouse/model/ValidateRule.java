package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.ValidateException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parent;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateRule extends Validatable{
    public static final String[] singleCandidates = new String[]{
            "一元函数1",
            "一元函数2"
    };

    public static final String[] compoundSingleCandidates = new String[]{
            "复合一元函数1",
            "复合一元函数2"
    };

    public static final String[] compoundTwoCandidates = new String[]{
            "复合二元函数1",
            "复合二元函数2"
    };

    public static final String[] compoundMultiCandidates = new String[]{
            "复合多元函数1",
            "复合多元函数2"
    };

    @NotBlank
    private String name;

    @NotNull
    private String serializedValues;

    @NotNull
    private ValidateRuleType validateRuleType;

    @Parent
    @JsonIgnore
    @Setter
    @Getter
    private Question question;

    ValidateRule(String name, String serializedValues, ValidateRuleType validateRuleType, Question question) {
        this.name = name;
        this.serializedValues = serializedValues;
        this.validateRuleType = validateRuleType;
        this.question = question;
    }

    @Override
    public void validate() {
        if (validateRuleType == ValidateRuleType.single) {
            if (Arrays.stream(singleCandidates).noneMatch(item -> item.equals(name))) {
                throw new ValidateException("错误的校验函数名称!");
            }

            if (!(question instanceof CompletionQuestion)) {
                throw new ValidateException("'" + name + "'" + " 只能用于填空题!");
            }
        } else {
            if (!(question instanceof CompoundQuestion)) {
                throw new ValidateException("'" + name + "'" + " 只能用于复合题!");
            }

            Set<String> testSet = new HashSet<>();
            List<String> valuesList = Arrays.asList(serializedValues.split(","));
            testSet.addAll(valuesList);

            if (testSet.size() != valuesList.size()) {
                throw new ValidateException("操作项目不能有重复项!");
            }

            if (validateRuleType == ValidateRuleType.compoundSingle) {
                if (valuesList.size() != 1) {
                    if (Arrays.stream(compoundSingleCandidates).noneMatch(item -> item.equals(name))) {
                        throw new ValidateException("错误的校验函数名称!");
                    }

                    throw new ValidateException("一元函数的参数只能是一位!");
                }
            } else if (validateRuleType == ValidateRuleType.compoundTwo) {
                if (valuesList.size() != 2) {
                    if (Arrays.stream(compoundTwoCandidates).noneMatch(item -> item.equals(name))) {
                        throw new ValidateException("错误的校验函数名称!");
                    }

                    throw new ValidateException("二元函数的参数只能是两位!");
                }
            } else if (validateRuleType == ValidateRuleType.compoundMulti) {
                if (Arrays.stream(compoundMultiCandidates).noneMatch(item -> item.equals(name))) {
                    throw new ValidateException("错误的校验函数名称!");
                }

                if (valuesList.size() == 0) {
                    throw new ValidateException("必须要有参数!");
                }
            }

            // values要有对应项目
            CompoundQuestion compoundQuestion = (CompoundQuestion) question;
            Stream<String> titles = compoundQuestion.getQuestions().stream().map(subQuestion -> subQuestion.gainTitleDefaultText());

            for (String value : valuesList) {
                if (titles.noneMatch(text -> text.equals(value))) {
                    throw new ValidateException("子问题中没有" + value + "这一项!");
                }
            }
        }
    }
}
