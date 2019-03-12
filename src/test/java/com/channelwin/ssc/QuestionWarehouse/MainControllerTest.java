package com.channelwin.ssc.QuestionWarehouse;

import com.channelwin.ssc.QuestionWarehouse.controller.MainController;
import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MainControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    // 目录
    Category category1 = new Category("目录1", 0);

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


    // 目录
    // 添加目录
    @Test
    public void addCategory() throws Exception {
        String baseUrl = "http://localhost:" + port + "/questionWarehouse/category";

        MainController.CategoryDTO dto1 = new MainController.CategoryDTO("目录1", 2.0);
        MainController.CategoryDTO dto2 = new MainController.CategoryDTO("目录2", 1.0);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .content(new ObjectMapper().writeValueAsString(dto1))
                .contentType(MediaType.APPLICATION_JSON)
        );

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .content(new ObjectMapper().writeValueAsString(dto2))
                .contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        JSONAssert.assertEquals("[ {\n" +
                "  \"title\" : {\n" +
                "    \"id\" : 4,\n" +
                "    \"defaultText\" : \"目录2\",\n" +
                "    \"translation\" : { }\n" +
                "  },\n" +
                "  \"seq\" : 1.0\n" +
                "}, {\n" +
                "  \"title\" : {\n" +
                "    \"id\" : 2,\n" +
                "    \"defaultText\" : \"目录1\",\n" +
                "    \"translation\" : { }\n" +
                "  },\n" +
                "  \"seq\" : 2.0\n" +
                "} ]", result.getResponse().getContentAsString(), JSONCompareMode.LENIENT);
    }

    // 删除目录
    // 编辑目录
    // 获取单个目录
    // 获取多个目录
    // end 目录

    // 题目
    // 添加题目
    // 删除题目
    // 编辑题目
    // 获取单个题目
    // 获取多个题目
    // end 题目

    // 获取入职试卷

}
