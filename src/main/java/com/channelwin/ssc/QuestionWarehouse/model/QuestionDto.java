package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.model.validator.TypeConstraint;
import com.channelwin.ssc.QuestionWarehouse.model.validator.TypeGroup;
import com.channelwin.ssc.Validatable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@TypeConstraint(groups = TypeGroup.class)
public class QuestionDto extends Validatable {
    @NotNull
    QuestionType questionType;

    @NotNull
    String titleDefaultText;

    @NotNull
    Double seq;

    Integer categoryId;

    String fitRule;

    String[] validateRules;

    String[] optionDefaultTexts;

    Integer minNum;

    Integer maxNum;

    QuestionDto[] questionDtos;

    // 填空, 判断题
    public QuestionDto(QuestionType questionType,
                       String titleDefaultText,
                       Double seq,
                       Integer categoryId,
                       String fitRule,
                       String[] validateRules) {
        this.questionType = questionType;
        this.titleDefaultText = titleDefaultText;
        this.seq = seq;
        this.categoryId = categoryId;
        this.fitRule = fitRule;
        this.validateRules = validateRules;
    }

    // 选择题
    public QuestionDto(String titleDefaultText,
                       Double seq,
                       Integer categoryId,
                       String fitRule,
                       String[] validateRules,
                       String... optionDefaultTexts) {
        this(QuestionType.choice, titleDefaultText, seq, categoryId, fitRule, validateRules);
        this.optionDefaultTexts = optionDefaultTexts;
    }

    // 复合题
    public QuestionDto(String titleDefaultText,
                       Double seq,
                       Integer categoryId,
                       String fitRule,
                       String[] validateRules,
                       Integer minNum,
                       Integer maxNum,
                       QuestionDto... questionDtos) {
        this(QuestionType.compound, titleDefaultText, seq, categoryId, fitRule, validateRules);

        this.minNum = minNum;
        this.maxNum = maxNum;

        this.questionDtos = questionDtos;
    }

    // 子问题
    public QuestionDto(QuestionType questionType,
                       String titleDefaultText,
                       double seq,
                       String[] validateRules) {
        this.questionType = questionType;
        this.titleDefaultText = titleDefaultText;
        this.seq = seq;
        this.validateRules = validateRules;
    }

    public QuestionDto(String titleDefaultText,
                       String[] validateRules,
                       double seq,
                       String... optionDefaultTexts) {
        this(QuestionType.choice, titleDefaultText, seq, validateRules);
        this.optionDefaultTexts = optionDefaultTexts;
    }

    public List<ValidateRule> generateValidateRules() {
        List<ValidateRule> result = new ArrayList<>();
        if (validateRules != null) {
            for (String item : validateRules) {
                result.add(FactoryService.createValidateRule(item));
            }
        }

        return result;
    }
}