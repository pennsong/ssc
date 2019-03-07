package com.channelwin.ssc;

import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.google.gson.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Type;

@SpringBootApplication
public class SscApplication {

    public static void main(String[] args) {
        SpringApplication.run(SscApplication.class, args);
    }

    @Bean
    public Gson getQuestionGsonSerializer() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

        return gson;
    }

    @Bean
    public JsonDeserializer<Question> getQuestionJsonDeserializer() {
        return new JsonDeserializer<Question>() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Question.class, this)
                    .create();

            @Override
            public Question deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();

                if (jsonObject.get("questionType").getAsString().equals(QuestionType.completion.toString())) {
                    return gson.fromJson(jsonObject, CompletionQuestion.class);
                }

                if (jsonObject.get("questionType").getAsString().equals(QuestionType.judgement.toString())) {
                    return gson.fromJson(jsonObject, JudgementQuestion.class);
                }

                if (jsonObject.get("questionType").getAsString().equals(QuestionType.choice.toString())) {
                    return gson.fromJson(jsonObject, ChoiceQuestion.class);
                }

                if (jsonObject.get("questionType").getAsString().equals(QuestionType.compound.toString())) {
                    return gson.fromJson(jsonObject, CompoundQuestion.class);
                }

                throw new ValidateException("错误的问题类型!");
            }
        };
    }

    @Bean
    public Gson getQuestionGsonDeserializer(JsonDeserializer<Question> deserializer) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Question.class, deserializer)
                .create();

        return gson;
    }
}
