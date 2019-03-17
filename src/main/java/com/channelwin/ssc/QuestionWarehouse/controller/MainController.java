package com.channelwin.ssc.QuestionWarehouse.controller;

import com.channelwin.ssc.Dto;
import com.channelwin.ssc.Gender;
import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
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
    public void editMultiLang(@PathVariable int id, @RequestBody @Valid MainController.MultiLangDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.toString());
        }

        dto.validate();

        Optional<MultiLang> optionalMultiLang = multiLangRepository.findById(id);

        if (optionalMultiLang.isPresent() == false) {
            throw new ValidateException("没有对应此id的多语言!");
        }

        MultiLang multiLang = optionalMultiLang.get();

        for (Map.Entry<Lang, String> item : dto.getTranslation().entrySet()) {
            multiLang.setText(item.getKey(), item.getValue());
        }
    }


    // 目录
    // 添加目录
    @RequestMapping(path = "/category", method = RequestMethod.POST)
    public void addCategory(@RequestBody @Valid CategoryDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.toString());
        }

        dto.validate();

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
    public void editCategory(@PathVariable int id, @RequestBody @Valid CategoryEditDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.toString());
        }

        dto.validate();

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent() == false) {
            throw new ValidateException("没有对应此id的目录!");
        }

        Category category = optionalCategory.get();
        category.setSeq(dto.seq);
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
    public void addQuestion(@Valid @RequestBody QuestionDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.toString());
        }

        dto.validate();

        Question question = factoryService.createQuestionFromQuestionDto(dto);
        questionRepository.save(question);
    }

    // 删除题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.DELETE)
    public void deleteQuestion(@PathVariable int id) {
        questionRepository.deleteById(id);
    }

    // 编辑题目
    @RequestMapping(path = "/question/{id}", method = RequestMethod.PUT)
    public void editQuestion(@PathVariable int id, @Valid @RequestBody QuestionEditDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.toString());
        }

        dto.validate();

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

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class CategoryDTO extends Dto {
        @NotNull
        String defaultText;

        @NotNull
        Double seq;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class CategoryEditDTO extends Dto {
        @NotNull
        Double seq;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class QuestionDto extends Dto {
        @NotNull
        QuestionType questionType;

        @NotNull
        String titleDefaultText;

        @NotNull
        Double seq;

        Integer categoryId;

        String fitRule;

        String[] validateRules;

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
                           String[] validateRules) {
            this.questionType = questionType;
            this.titleDefaultText = titleDefaultText;
            this.seq = seq;
            this.categoryId = categoryId;
            this.fitRule = fitRule;
            this.validateRules = validateRules;
        }

        // 选择题
        public QuestionDto(String titleDefaultText,
                           Double seq,
                           Integer categoryId,
                           String fitRule,
                           String[] validateRules,
                           String... optionDefaultTexts) {
            this(QuestionType.choice, titleDefaultText, seq, categoryId, fitRule, validateRules);
            this.optionDefaultTexts = optionDefaultTexts;
        }

        // 复合题
        public QuestionDto(String titleDefaultText,
                           Double seq,
                           Integer categoryId,
                           String fitRule,
                           String[] validateRules,
                           Integer minNum,
                           Integer maxNum,
                           QuestionDto... questionDtos) {
            this(QuestionType.compound, titleDefaultText, seq, categoryId, fitRule, validateRules);

            this.minNum = minNum;
            this.maxNum = maxNum;

            this.questionDtos = questionDtos;
        }

        // 子问题
        public QuestionDto(QuestionType questionType,
                           String titleDefaultText,
                           double seq,
                           String[] validateRules) {
            this.questionType = questionType;
            this.titleDefaultText = titleDefaultText;
            this.seq = seq;
            this.validateRules = validateRules;
        }

        public QuestionDto(String titleDefaultText,
                           String[] validateRules,
                           double seq,
                           String... optionDefaultTexts) {
            this(QuestionType.choice, titleDefaultText, seq, validateRules);
            this.optionDefaultTexts = optionDefaultTexts;
        }

        public List<ValidateRule> generateValidateRules() {
            List<ValidateRule> result = new ArrayList<>();
            if (validateRules != null ) {
                for (String item : validateRules) {
                    result.add(FactoryService.createValidateRule(item));
                }
            }

            return result;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class QuestionEditDto extends Dto {
        @NotNull
        Double seq;

        Integer categoryId;

        QuestionEditDto[] questionEditDtos;

        @Override
        public void validate() {
            if (categoryId == null) {
                throw new ValidateException("目录id不能为空!");
            }

            if (questionEditDtos != null) {
                for (QuestionEditDto item : questionEditDtos) {
                    item.subValidate();
                }
            }
        }

        public void subValidate() {
            if (categoryId != null) {
                throw new ValidateException("子问题不能有目录id!");
            }
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class MultiLangDto extends Dto {
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

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class EmployeeFixItemDto extends Dto {
        @NotNull
        String IDCardNum;

        @NotNull
        String name;

        @NotNull
        Gender gender;
    }

}