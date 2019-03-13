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
            Category category1 = new Category("目录1", 1.1);

            category1 = categoryRepository.save(category1);

            for (int i = 2; i <= 20; i++) {
               categoryRepository.save(new Category("目录" + i, i));
            }

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
//            "#gender == T(com.channelwin.ssc.Gender).MALE",
                    compoundCompletionQuestion1,
                    compoundJudgementQuestion1,
                    compoundChoiceQuestion1
            );

            questionRepository.save(completionQuestion1);
            questionRepository.save(judgementQuestion1);
            questionRepository.save(choiceQuestion1);
            questionRepository.save(compoundQuestion1);

            log.info("initData done!");
        };
    }
}
