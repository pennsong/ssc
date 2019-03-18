package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.model.validator.TypeConstraint;
import com.channelwin.ssc.ValidateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@TypeConstraint
public class QuestionEditDto extends Validatable {
    @NotNull
    Double seq;

    Integer categoryId;

    QuestionEditDto[] questionEditDtos;

    @Override
    public void validate() {
        if (categoryId == null) {
            throw new ValidateException("目录id不能为空!");
        }

        if (questionEditDtos != null) {
            for (QuestionEditDto item : questionEditDtos) {
                item.subValidate();
            }
        }
    }

    public void subValidate() {
        if (categoryId != null) {
            throw new ValidateException("子问题不能有目录id!");
        }
    }
}
