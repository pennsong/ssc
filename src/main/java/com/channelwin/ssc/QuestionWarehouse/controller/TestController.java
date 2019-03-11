package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private MultiLangRepository multiLangRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @RequestMapping("/test2")
    public CompoundQuestion test2() throws Exception {
        Category category = new Category("目录1", 0, "PY:目录1 PY", "EN: 目录1 EN");
        category = categoryRepository.save(category);

        Question cq1 = new CompletionQuestion("复合填空1");
        Question cq2 = new JudgementQuestion("复合判断1");
        Question cq3 = new ChoiceQuestion("复合选择1", "复合选择1选项1", "复合选择1选项2");

        CompoundQuestion q1 = new CompoundQuestion("复合题1", category, "gender == T(com.channelwin.ssc.Gender).MALE", cq1, cq2, cq3);
        q1.setMaxNum(5);
        q1.setMinNum(3);

        q1 = questionRepository.save(q1);

        return q1;
    }

    @RequestMapping("/test1")
    public String test1() throws Exception {
        throw new Exception("test");

//        return "ok";
    }
}
