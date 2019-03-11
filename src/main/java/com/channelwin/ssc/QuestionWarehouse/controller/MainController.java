package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.EntryMaterialCollecting.EmployeeFixItem;
import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import com.channelwin.ssc.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @RequestMapping(path = "/category/add", method = RequestMethod.POST)
    public void addCategory(@RequestBody @Valid CategoryEditDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors());
        }
        log.info(bindingResult.toString());
        Category category = new Category(dto.defaultText, dto.seq);
        categoryRepository.save(category);
    }

    // 删除目录
    @RequestMapping(path = "/category/delete/{id}", method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable int id) {
        categoryRepository.deleteById(id);
    }

    // 编辑目录
    @RequestMapping(path = "/category/delete/{id}", method = RequestMethod.PUT)
    public void editCategory(HttpServletRequest request, @PathVariable int id) {
        Category category =  categoryRepository.findById(id).get();
//        category.setDefaultText(defaultText);
//        category.setSeq(seq);

        categoryRepository.save(category);
    }
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

    @RequestMapping(path = "/getEntryPaper2", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Question> getEntryPaper2() {
        return questionRepository.findAll();
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
}
