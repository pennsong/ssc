package com.channelwin.ssc.QuestionWarehouse.model.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, TypeGroup.class})
public interface Group {
}
