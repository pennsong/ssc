package com.channelwin.ssc;

import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class SscApplication {

    public static void main(String[] args) {
        SpringApplication.run(SscApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder() {

            @Override
            public void configure(ObjectMapper objectMapper) {
                super.configure(objectMapper);
                objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            }

        };
    }

    @Bean
    public CommandLineRunner initData(CategoryRepository categoryRepository, QuestionRepository questionRepository) {
        return args -> {
            // 目录
            Category category1 = FactoryService.createCategory("目录1", 1.1);

            category1 = categoryRepository.save(category1);

            for (int i = 2; i <= 20; i++) {
               categoryRepository.save( FactoryService.createCategory("目录" + i, new Double(i)));
            }

            // 填空题
            CompletionQuestion completionQuestion1 = FactoryService.createCompletionQuestion("填空题1", category1);

            // 判断题
            JudgementQuestion judgementQuestion1 = FactoryService.createJudgementQuestion("判断题1", category1);

            // 选择题
            ChoiceQuestion choiceQuestion1 = FactoryService.createChoiceQuestion("选择题1", category1, "选择题1_选项1", "选择题1_选项2");

            // 复合题
            CompletionQuestion compoundCompletionQuestion1 = FactoryService.createCompletionQuestion("复合填空题1", category1);
            JudgementQuestion compoundJudgementQuestion1 = FactoryService.createJudgementQuestion("复合判断题1", category1);
            ChoiceQuestion compoundChoiceQuestion1 = FactoryService.createChoiceQuestion("复合选择题1", category1, "复合选择题1_选项1", "复合选择题1_选项2");

            List<Question> questionList = new ArrayList();
            questionList.add(compoundCompletionQuestion1);
            questionList.add(compoundJudgementQuestion1);
            questionList.add(compoundChoiceQuestion1);

            CompoundQuestion compoundQuestion1 = FactoryService.createCompoundQuestion(
                    "复合题1",
                    category1,
                    questionList
            );

            questionRepository.save(completionQuestion1);
            questionRepository.save(judgementQuestion1);
            questionRepository.save(choiceQuestion1);
            questionRepository.save(compoundQuestion1);

            log.info("initData done!");
        };
    }
}
