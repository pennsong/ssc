package com.channelwin.ssc.QuestionWarehouse.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validatable {
    public void validate() {
        log.info(this.toString() + " validate");
    }
}
