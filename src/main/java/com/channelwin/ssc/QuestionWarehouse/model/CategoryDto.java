package com.channelwin.ssc.QuestionWarehouse.model;

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
public class CategoryDto extends Validatable {
    @NotEmpty
    String defaultText;

    @NotNull
    Double seq;
}
