package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.EntryMaterialCollecting.EmployeeFixItem;
import com.channelwin.ssc.QuestionWarehouse.model.Category;
import com.channelwin.ssc.QuestionWarehouse.model.Question;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.CompoundQuestionRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.channelwin.ssc.Gender.FEMALE;

@Slf4j
@RestController
@RequestMapping("/questionWarehouse")
public class MainController {
    private MultiLangRepository multiLangRepository;

    private CategoryRepository categoryRepository;

    private CompoundQuestionRepository compoundQuestionRepository;

    private JsonDeserializer<Question> deserializer;

    private Gson questionGsonSerializer;

    private Gson questionGsonDeserializer;

    public MainController(MultiLangRepository multiLangRepository,
                          CategoryRepository categoryRepository,
                          CompoundQuestionRepository compoundQuestionRepository,
                          JsonDeserializer<Question> deserializer,
                          @Qualifier("getQuestionGsonSerializer")
                                  Gson questionGsonSerializer,
                          @Qualifier("getQuestionGsonDeserializer")
                                  Gson questionGsonDeserializer
    ) {
        this.categoryRepository = categoryRepository;
        this.compoundQuestionRepository = compoundQuestionRepository;
        this.deserializer = deserializer;
        this.questionGsonSerializer = questionGsonSerializer;
        this.questionGsonDeserializer = questionGsonDeserializer;
    }

    // 目录
    // 添加目录
    @RequestMapping(path = "/category/add", method = RequestMethod.POST)
    public void addCategory(String defaultText) {
        Category category = new Category(defaultText, 0);
        categoryRepository.save(category);
    }

    // todo 删除目录
    @RequestMapping(path = "/category/delete", method = RequestMethod.DELETE)
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
    // todo 编辑目录
    // todo 获取目录
    // end 目录

    // 题目
    // todo 添加题目
    // todo 删除题目
    // todo 编辑题目
    // todo 获取题目
    // end 题目


    // todo 获取入职试卷
    @RequestMapping(path = "/getEntryPaper", method = RequestMethod.GET)
    @ResponseBody
    public List<Question> getEntryPaper() {
        EmployeeFixItem employeeFixItem = new EmployeeFixItem("idCardNo1", "姓名1", FEMALE);
        List<Question> list = StreamSupport.stream(compoundQuestionRepository.findAll().spliterator(), false)
                .filter(question -> {
                    ExpressionParser parser = new SpelExpressionParser();
                    Expression expression = parser.parseExpression(question.getFitRule());
                    EvaluationContext context = new StandardEvaluationContext(employeeFixItem);
                    return (boolean) expression.getValue(context);
                })
                .collect(Collectors.toList());

        return list;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }
}
