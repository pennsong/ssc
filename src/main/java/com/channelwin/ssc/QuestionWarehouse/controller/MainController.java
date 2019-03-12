package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.EntryMaterialCollecting.EmployeeFixItem;
import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import com.channelwin.ssc.ValidateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.channelwin.ssc.Gender.MALE;

@Slf4j
@RestController
@RequestMapping("/questionWarehouse")
public class MainController {
    private MultiLangRepository multiLangRepository;

    private CategoryRepository categoryRepository;

    private QuestionRepository questionRepository;

    public MainController(MultiLangRepository multiLangRepository,
                          CategoryRepository categoryRepository,
                          QuestionRepository questionRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    // 目录
    // 添加目录
    @RequestMapping(path = "/category", method = RequestMethod.POST)
    public void addCategory(@RequestBody @Valid CategoryDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.toString());
        }

        Category category = new Category(dto.defaultText, dto.seq);
        categoryRepository.save(category);
    }

    // 删除目录
    @RequestMapping(path = "/category/{id}", method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable int id) {
        categoryRepository.deleteById(id);
    }

    // 编辑目录
    @RequestMapping(path = "/category/{id}", method = RequestMethod.PUT)
    public void editCategory(@PathVariable int id, @RequestBody @Valid CategoryDTO dto) {
        Category category = categoryRepository.findById(id).get();
        category.setDefaultText(dto.defaultText);
        category.setSeq(dto.seq);

        categoryRepository.save(category);
    }

    // 获取单个目录
    @RequestMapping(path = "/category/{id}")
    public Category getCategory(@PathVariable int id) {
        Category category = categoryRepository.findById(id).get();

        return category;
    }

    // 获取多个目录
    @RequestMapping(path = "/category")
    public Iterable<Category> getCategories() {
        return categoryRepository.findAll();
    }
    // end 目录

    // 题目
    // 添加题目
    @RequestMapping(path = "/question", method = RequestMethod.POST)
    public void addQuestion(@Valid @RequestBody QuestionDto questionDto) {

    }

    // 删除题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.DELETE)
    public void deleteQuestion(int id) {

    }
    // 编辑题目
    @RequestMapping(path = "/question", method = RequestMethod.PUT)
    public void editQuestion(@Valid @RequestBody QuestionDto questionDto) {

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

        return  questionRepository.findAll(pageable);
    }
    // end 题目


    // 获取入职试卷
    @RequestMapping(path = "/getEntryPaper", method = RequestMethod.GET)
    public List<Question> getEntryPaper() {
        EmployeeFixItem employeeFixItem = new EmployeeFixItem("idCardNo1", "姓名1", MALE);
        List<Question> list = StreamSupport.stream(questionRepository.findAll().spliterator(), false)
                .filter(question -> {
                    ExpressionParser parser = new SpelExpressionParser();
                    Expression expression = parser.parseExpression(question.getFitRule());
                    EvaluationContext context = new StandardEvaluationContext(employeeFixItem);
                    return (boolean) expression.getValue(context);
                })
                .collect(Collectors.toList());

        return list;
    }

    @RequestMapping("/initData")
    @ResponseBody
    public String initData() {
        Category category1 = categoryRepository.findById(1).get();

        // 填空题
        CompletionQuestion completionQuestion1 = new CompletionQuestion("填空题1");

        // 判断题
        JudgementQuestion judgementQuestion1 = new JudgementQuestion("判断题1");

        // 选择题
        ChoiceQuestion choiceQuestion1 = new ChoiceQuestion("选择题1");

        // 复合题
        CompletionQuestion compoundCompletionQuestion1 = new CompletionQuestion("复合填空题1");
        JudgementQuestion compoundJudgementQuestion1 = new JudgementQuestion("复合判断题1");
        ChoiceQuestion compoundChoiceQuestion1 = new ChoiceQuestion("复合选择题1");

        CompoundQuestion compoundQuestion1 = new CompoundQuestion(
                "复合题!",
                category1,
                "gender == T(com.channelwin.ssc.Gender).MALE",
                compoundCompletionQuestion1,
                compoundJudgementQuestion1,
                compoundChoiceQuestion1
        );

        questionRepository.save(compoundQuestion1);

        return "initData OK";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDTO {
        @NotNull
        String defaultText;

        @NotNull
        Double seq;
    }

    @Data
    @NoArgsConstructor
//    @AllArgsConstructor
    public static class QuestionDto {

    }
}