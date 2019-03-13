package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.controller.MainController;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.ValidateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class FactoryService {

    private CategoryRepository categoryRepository;

    public FactoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private Category getCategory(MainController.QuestionDto questionDto) {
        Category category = null;
        if (questionDto.getCategoryId() != null) {
            Optional<Category> optionalCategory = this.categoryRepository.findById(questionDto.getCategoryId());

            if (optionalCategory.isPresent() == false) {
                throw new ValidateException("所需目录不存在!");
            } else {
                category = optionalCategory.get();
            }
        }
        return category;
    }

    // MultiLang
    public static MultiLang createMuliLang(String defaultText) {
        return new MultiLang(null, defaultText, new HashMap<>());
    }

    // 选项
    public static Option createOption(Integer key, String optionDefaultText) {
        return new Option(key, createMuliLang(optionDefaultText), 0);
    }

    public static Option createOption(Integer key, String optionDefaultText, Integer score) {
        Option option = createOption(key, optionDefaultText);
        option.setScore(score);

        return option;
    }

    // 选项list
    public static List<Option> createOptionList(String... optionDefaultTexts) {
        List<Option> optionList = new ArrayList<>();

        for (int i = 0; i < optionDefaultTexts.length; i++) {
            optionList.add(createOption(i, optionDefaultTexts[i]));
        }

        return optionList;
    }

    // end 选项

    // 目录
    public static Category createCategory(String titleDefaultText) {
        MultiLang title = createMuliLang(titleDefaultText);
        return new Category(null, title, 0.0);
    }

    public static Category createCategory(String titleDefaultText, Double seq) {
        Category category = createCategory(titleDefaultText);
        category.setSeq(seq);

        return category;
    }

    // 问题
    // 填空题
    public static CompletionQuestion createCompletionQuestion(String titleDefaultText) {
        MultiLang title = createMuliLang(titleDefaultText);

        return new CompletionQuestion(title, 0.0, QuestionType.completion, null, false, null, null, null);
    }

    public static CompletionQuestion createCompletionQuestion(String titleDefaultText, Category category) {
        CompletionQuestion question = createCompletionQuestion(titleDefaultText);
        question.setCategory(category);

        return question;
    }

    public static CompletionQuestion createCompletionQuestion(String titleDefaultText, Category category, Double seq) {
        CompletionQuestion question = createCompletionQuestion(titleDefaultText, category);
        question.setSeq(seq);

        return question;
    }

    public static CompletionQuestion createCompletionQuestion(String titleDefaultText, Category category, Double seq, String fitRule, String validateRule) {
        CompletionQuestion question = createCompletionQuestion(titleDefaultText, category, seq);
        question.setFitRule(fitRule);
        question.setValidateRule(validateRule);

        return question;
    }

    // 判断题
    public static JudgementQuestion createJudgementQuestion(String titleDefaultText) {
        MultiLang title = createMuliLang(titleDefaultText);

        return new JudgementQuestion(title, 0.0, QuestionType.judgement, null, false, null, null, null);
    }

    public static JudgementQuestion createJudgementQuestion(String titleDefaultText, Category category) {
        JudgementQuestion question = createJudgementQuestion(titleDefaultText);
        question.setCategory(category);

        return question;
    }

    public static JudgementQuestion createJudgementQuestion(String titleDefaultText, Category category, Double seq) {
        JudgementQuestion question = createJudgementQuestion(titleDefaultText, category);
        question.setSeq(seq);

        return question;
    }

    public static JudgementQuestion createJudgementQuestion(String titleDefaultText, Category category, Double seq, String fitRule, String validateRule) {
        JudgementQuestion question = createJudgementQuestion(titleDefaultText, category, seq);
        question.setFitRule(fitRule);
        question.setValidateRule(validateRule);

        return question;
    }

    // 选择题
    public static ChoiceQuestion createChoiceQuestion(String titleDefaultText, String... options) {
        MultiLang title = createMuliLang(titleDefaultText);

        List<Option> optionList = createOptionList(options);

        return new ChoiceQuestion(optionList, false, title, 0.0, QuestionType.choice, null, false, null, null, null);
    }

    public static ChoiceQuestion createChoiceQuestion(String titleDefaultText, Category category, String... options) {
        ChoiceQuestion question = createChoiceQuestion(titleDefaultText, options);
        question.setCategory(category);

        return question;
    }

    public static ChoiceQuestion createChoiceQuestion(String titleDefaultText, Category category, Double seq, String... options) {
        ChoiceQuestion question = createChoiceQuestion(titleDefaultText, category, options);
        question.setSeq(seq);

        return question;
    }

    public static ChoiceQuestion createChoiceQuestion(String titleDefaultText, Category category, String fitRule, String validateRule, Double seq, String... options) {
        ChoiceQuestion question = createChoiceQuestion(titleDefaultText, category, seq, options);
        question.setFitRule(fitRule);
        question.setValidateRule(validateRule);

        return question;
    }

    // 复合题
    public static CompoundQuestion createCompoundQuestion(String titleDefaultText, Category category, List<Question> questions) {
        MultiLang title = createMuliLang(titleDefaultText);

        CompoundQuestion question = new CompoundQuestion(1, 10, questions, title, 0.0, QuestionType.compound, category, null, null);

        return question;
    }

    public static CompoundQuestion createCompoundQuestion(String titleDefaultText, Category category, List<Question> questions, Double seq) {
        CompoundQuestion question = createCompoundQuestion(titleDefaultText, category, questions);
        question.setSeq(seq);

        return question;
    }

    public static CompoundQuestion createCompoundQuestion(String titleDefaultText, Category category, List<Question> questions, Double seq, Integer minNum, Integer maxNum) {
        CompoundQuestion question = createCompoundQuestion(titleDefaultText, category, questions, seq);
        question.setMinNum(minNum);
        question.setMaxNum(maxNum);

        return question;
    }

    public static CompoundQuestion createCompoundQuestion(String titleDefaultText, Category category, List<Question> questions, Double seq, Integer minNum, Integer maxNum, String fitRule, String validateRule) {
        CompoundQuestion question = createCompoundQuestion(titleDefaultText, category, questions, seq);
        question.setMinNum(minNum);
        question.setMaxNum(maxNum);
        question.setFitRule(fitRule);
        question.setValidateRule(validateRule);

        return question;
    }

    // from questionDto
    public Question createQuestionFromQuestionDto(MainController.QuestionDto questionDto) {
        Category category = getCategory(questionDto);

        switch (questionDto.getQuestionType()) {
            case completion:
                return createCompletionQuestion(questionDto.getTitleDefaultText(), category, questionDto.getSeq(), questionDto.getFitRule(), questionDto.getValidateRule());
            case judgement:
                return createJudgementQuestion(questionDto.getTitleDefaultText(), category, questionDto.getSeq(), questionDto.getFitRule(), questionDto.getValidateRule());
            case choice:
                return createChoiceQuestion(questionDto.getTitleDefaultText(), category, questionDto.getFitRule(), questionDto.getValidateRule(), questionDto.getSeq(), questionDto.getOptionDefaultTexts());
            case compound:
                List<Question> questionList = new ArrayList<>();
                for (MainController.QuestionDto item: questionDto.getQuestionDtos()) {
                    Question question = createQuestionFromQuestionDto(item);
                    questionList.add(question);
                }
                return createCompoundQuestion(questionDto.getTitleDefaultText(), category, questionList, questionDto.getSeq(), questionDto.getMinNum(), questionDto.getMaxNum(),  questionDto.getFitRule(),  questionDto.getValidateRule());
            default:
                throw new ValidateException("错误的问题类型!");
        }
    }

    // end 问题
}