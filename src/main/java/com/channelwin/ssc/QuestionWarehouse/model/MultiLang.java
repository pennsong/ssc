package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.Validatable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiLang extends Validatable {
    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 100)
    private String defaultText;

    @ElementCollection
    @NotNull
    private Map<Lang, String> translation = new HashMap<>();

    MultiLang(String defaultText, Map<Lang, String> translation) {
        this.defaultText = defaultText;
        if (translation == null) {
            this.translation = new HashMap<>();
        } else {
            this.translation = translation;
        }
    }

    public void setText(Lang lang, String text) {
        translation.put(lang, text);
    }

    @Override
    public String toString() {
        return defaultText;
    }
}
