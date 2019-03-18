package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.model.validator.TypeConstraint;
import com.channelwin.ssc.ValidateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@TypeConstraint
public class MultiLangDto extends Validatable {
    @NotNull
    Map<Lang, String> translation;

    public MultiLangDto(String... translationTexts) {
        this.translation = new HashMap<>();

        for (String item: translationTexts) {
            String[] stringArray = item.split(":");
            this.translation.put(Lang.valueOf(stringArray[0].trim()), stringArray[1].trim());
        }
    }

    @Override
    public void validate() {
        for (Map.Entry<Lang, String> item : translation.entrySet()) {
            if (StringUtils.isEmpty(item.getValue())) {
                throw new ValidateException("翻译字段不能为空!");
            }
        }
    }
}