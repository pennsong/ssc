package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import com.channelwin.ssc.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequestMapping("/questionWarehouse")
@Transactional
public class MainController {
    private FactoryService factoryService;

    private MultiLangRepository multiLangRepository;

    private CategoryRepository categoryRepository;

    private QuestionRepository questionRepository;

    public MainController(
            FactoryService factoryService,
            MultiLangRepository multiLangRepository,
            CategoryRepository categoryRepository,
            QuestionRepository questionRepository
    ) {
        this.factoryService = factoryService;
        this.multiLangRepository = multiLangRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    // MultiLang
    @RequestMapping(path = "/multiLang/{id}", method = RequestMethod.PUT)
    public void editMultiLang(@PathVariable int id, @RequestBody MultiLangDto dto) {
        Optional<MultiLang> optionalMultiLang = multiLangRepository.findById(id);

        if (optionalMultiLang.isPresent() == false) {
            throw new ValidateException("没有对应此id的多语言!");
        }

        MultiLang multiLang = optionalMultiLang.get();

        for (Map.Entry<Lang, String> item : dto.getTranslation().entrySet()) {
            multiLang.setText(item.getKey(), item.getValue());
        }

        multiLang.validate();
    }


    // 目录
    // 添加目录
    @RequestMapping(path = "/category", method = RequestMethod.POST)
    public void addCategory(@RequestBody CategoryDto dto) {
        Category category = FactoryService.createCategory(dto.getDefaultText(), dto.getSeq());
        category.validate();
        categoryRepository.save(category);
    }

    // 删除目录
    @RequestMapping(path = "/category/{id}", method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable int id) {
        categoryRepository.deleteById(id);
    }

    // 编辑目录
    @RequestMapping(path = "/category/{id}", method = RequestMethod.PUT)
    public void editCategory(@PathVariable int id, @RequestBody CategoryEditDto dto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent() == false) {
            throw new ValidateException("没有对应此id的目录!");
        }

        Category category = optionalCategory.get();
        category.setSeq(dto.getSeq());
        category.validate();
    }

    // 获取单个目录
    @RequestMapping(path = "/category/{id}")
    public Category getCategory(@PathVariable int id) {
        Category category = categoryRepository.findById(id).get();

        return category;
    }

    // 获取多个目录
    @RequestMapping(path = "/category")
    public Iterable<Category> getCategories(@RequestParam int pageNum, @RequestParam int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return categoryRepository.findAll(pageable);
    }
    // end 目录

    // 题目
    // 添加题目
    @RequestMapping(path = "/question", method = RequestMethod.POST)
    public void addQuestion(@RequestBody QuestionDto dto) {
        Question question = factoryService.createQuestionFromQuestionDto(dto);
        question.validate();
        questionRepository.save(question);
    }

    // 删除题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.DELETE)
    public void deleteQuestion(@PathVariable int id) {
        questionRepository.deleteById(id);
    }

    // 编辑题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.PUT)
    public void editQuestion(@PathVariable int id, @RequestBody QuestionEditDto dto) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if (optionalQuestion.isPresent() == false) {
            throw new ValidateException("没有对应此id的问题!");
        }

        Question question = optionalQuestion.get();

        question.setSeq(dto.getSeq());

        Optional<Category> optionalCategory = categoryRepository.findById(dto.getCategoryId());

        if (optionalCategory.isPresent() == false) {
            throw new ValidateException("没有对应此id的目录!");
        }

        Category category = optionalCategory.get();

        question.setCategory(category);

        question.validate();
    }

    // 获取单个题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.GET)
    public Question getQuestion(@PathVariable int id) {
        Question question = questionRepository.findById(id).get();

        return question;
    }

    // 获取多个题目
    @RequestMapping(path = "/question", method = RequestMethod.GET)
    public Page<Question> getQuestions(@RequestParam int pageNum, @RequestParam int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return questionRepository.findAllTopQuestion(pageable);
    }
    // end 题目

    // 获取入职试卷
    @RequestMapping(path = "/getEntryPaper", method = RequestMethod.POST)
    public List<Question> getEntryPaper(@RequestBody EmployeeFixItemDto employeeFixItemDto) {
        List<Question> list = StreamSupport.stream(questionRepository.findAll().spliterator(), false)
                .filter(question -> {
                    ExpressionParser parser = new SpelExpressionParser();
                    Expression expression = parser.parseExpression(question.getFitRule());
                    EvaluationContext context = new StandardEvaluationContext(employeeFixItemDto);
                    return (boolean) expression.getValue(context);
                })
                .collect(Collectors.toList());

        return list;
    }
}