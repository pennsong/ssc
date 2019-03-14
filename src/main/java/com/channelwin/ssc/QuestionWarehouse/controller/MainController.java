package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.EntryMaterialCollecting.EmployeeFixItem;
import com.channelwin.ssc.QuestionWarehouse.model.Category;
import com.channelwin.ssc.QuestionWarehouse.model.FactoryService;
import com.channelwin.ssc.QuestionWarehouse.model.Question;
import com.channelwin.ssc.QuestionWarehouse.model.QuestionType;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import com.channelwin.ssc.ValidateException;
import lombok.AccessLevel;
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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class MainController {
    private FactoryService factoryService;

    private CategoryRepository categoryRepository;

    private QuestionRepository questionRepository;

    public MainController(
            FactoryService factoryService,
            CategoryRepository categoryRepository,
            QuestionRepository questionRepository
    ) {
        this.factoryService = factoryService;
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

        Category category = FactoryService.createCategory(dto.defaultText, dto.seq);
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
    public Iterable<Category> getCategories(@RequestParam int pageNum, @RequestParam int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return categoryRepository.findAll(pageable);
    }
    // end 目录

    // 题目
    // 添加题目
    @RequestMapping(path = "/question", method = RequestMethod.POST)
    public void addQuestion(@Valid @RequestBody QuestionDto questionDto) {
        questionDto.validate();

        Question question = factoryService.createQuestionFromQuestionDto(questionDto);
        questionRepository.save(question);
    }

    // 删除题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.DELETE)
    public void deleteQuestion(@PathVariable int id) {
        questionRepository.deleteById(id);
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

        return questionRepository.findAllTopQuestion(pageable);
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

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class CategoryDTO {
        @NotNull
        String defaultText;

        @NotNull
        Double seq;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class QuestionDto {
        @NotNull
        QuestionType questionType;

        @NotNull
        String titleDefaultText;

        @NotNull
        Double seq;

        Integer categoryId;

        String fitRule;

        String validateRule;

        String[] optionDefaultTexts;

        Integer minNum;

        Integer maxNum;

        QuestionDto[] questionDtos;

        // 填空, 判断题
        public QuestionDto(QuestionType questionType,
                           String titleDefaultText,
                           Double seq,
                           Integer categoryId,
                           String fitRule,
                           String validateRule) {
            this.questionType = questionType;
            this.titleDefaultText = titleDefaultText;
            this.seq = seq;
            this.categoryId = categoryId;
            this.fitRule = fitRule;
            this.validateRule = validateRule;
        }

        // 选择题
        public QuestionDto(String titleDefaultText,
                           Double seq,
                           Integer categoryId,
                           String fitRule,
                           String validateRule,
                           String... optionDefaultTexts) {
            this(QuestionType.choice, titleDefaultText, seq, categoryId, fitRule, validateRule);
            this.optionDefaultTexts = optionDefaultTexts;
        }

        // 复合题
        public QuestionDto(String titleDefaultText,
                           Double seq,
                           Integer categoryId,
                           String fitRule,
                           String validateRule,
                           Integer minNum,
                           Integer maxNum,
                           QuestionDto... questionDtos) {
            this(QuestionType.compound, titleDefaultText, seq, categoryId, fitRule, validateRule);

            this.minNum = minNum;
            this.maxNum = maxNum;

            this.questionDtos = questionDtos;
        }

        // 子问题
        public QuestionDto(QuestionType questionType,
                           String titleDefaultText,
                           double seq,
                           String validateRule) {
            this.questionType = questionType;
            this.titleDefaultText = titleDefaultText;
            this.seq = seq;
            this.validateRule = validateRule;
        }

        public QuestionDto(String titleDefaultText,
                           String validateRule,
                           double seq,
                           String... optionDefaultTexts) {
            this(QuestionType.choice, titleDefaultText, seq, validateRule);
            this.optionDefaultTexts = optionDefaultTexts;
        }

        public void validate() {
            if (questionType.equals(QuestionType.choice)) {
                if (optionDefaultTexts == null || optionDefaultTexts.length < 2) {
                    throw new ValidateException("选择题的选项至少要有2项!");
                }
            } else if (questionType.equals(QuestionType.compound)) {
                if (questionDtos == null || questionDtos.length < 1) {
                    throw new ValidateException("复合题的子问题至少要有1项!");
                }

                for (QuestionDto item : questionDtos) {
                    item.subValidate();
                }
            }
        }

        public void subValidate() {
            if (questionType.equals(QuestionType.choice)) {
                if (optionDefaultTexts == null || optionDefaultTexts.length < 2) {
                    throw new ValidateException("选择题的选项至少要有2项!");
                }
            } else if (questionType.equals(QuestionType.compound)) {
                throw new ValidateException("子问题不能是复合题!");
            }
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class QuestionEditDto {
        @NotNull
        Double seq;

        Integer categoryId;

        QuestionEditDto[] questionEditDtos;

        public void validate() {
            if (categoryId == null) {
                throw new ValidateException("目录id不能为空!");
            }

            for (QuestionEditDto item : questionEditDtos) {
                item.subValidate();
            }
        }

        public void subValidate() {
            if (categoryId != null) {
                throw new ValidateException("子问题不能有目录id!");
            }
        }
    }
}