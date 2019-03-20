package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.Validatable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.Validation;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Category extends Validatable {
    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private MultiLang title;

    @Setter
    @NotNull
    @Min(0)
    private Double seq;

    @Transient
    private Set<MultiLang> list0;

    @Transient
    private List<MultiLang> list1;

    @Transient
    private Collection<MultiLang> list2;

    @Transient
    private Map<String, MultiLang> list3;

    @Transient
    private MultiLang[] list4;

    Category(MultiLang title, Double seq) {
        this.title = title;
        this.seq = seq;
    }

    @Override
    public void validate() {
        // 父类校验
        super.validate();

        // 单项校验
        Validation.buildDefaultValidatorFactory().getValidator()
                .validate(this);

        // 单项Validatable校验, ValidateIgnore的忽略
        Validatable.validateObjectValidatableFields(this);

        // 整体校验
        log.info(this.toString() + ":整体校验");
    }

    public void setDefaultText(String defaultText) {
        this.title.setDefaultText(defaultText);
    }
}