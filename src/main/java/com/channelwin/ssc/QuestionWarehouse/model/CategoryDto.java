package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.model.validator.TestFieldConstraint;
import com.channelwin.ssc.QuestionWarehouse.model.validator.TypeConstraint;
import com.channelwin.ssc.QuestionWarehouse.model.validator.TypeGroup;
import com.channelwin.ssc.Validatable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@TypeConstraint(groups = TypeGroup.class)
public class CategoryDto extends Validatable {
    @NotEmpty
    String defaultText;

    @NotNull
    @TestFieldConstraint
    Double seq;
}
