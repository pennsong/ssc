package com.channelwin.ssc.QuestionWarehouse.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CategoryEditDTO {
    @Min(value = 5)
    @Max(value = 8)
    String defaultText;

    @NotNull
    float seq;
}
