package com.channelwin.ssc.EntryMaterialCollecting;

import com.channelwin.ssc.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EmployeeFixItem {
    @Getter
    private String IDCardNum;

    @Getter
    private String name;

    @Getter
    private Gender gender;
}
