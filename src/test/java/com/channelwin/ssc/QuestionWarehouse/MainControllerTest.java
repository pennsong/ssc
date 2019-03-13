package com.channelwin.ssc.QuestionWarehouse;

import com.channelwin.ssc.QuestionWarehouse.controller.MainController;
import com.channelwin.ssc.QuestionWarehouse.model.Category;
import com.channelwin.ssc.QuestionWarehouse.model.Question;
import com.channelwin.ssc.QuestionWarehouse.model.QuestionType;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class MainControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private String baseUrl = "http://localhost:" + port + "/questionWarehouse";

    private String categoryBaseUrl = baseUrl + "/category";

    private String questionBaseUrl = baseUrl + "/question";

    @Autowired
    private ObjectMapper objectMapper;

    // 目录
    // 添加目录
    @Test
    public void addCategory() throws Exception {
        MainController.CategoryDTO dto1 = new MainController.CategoryDTO("目录t1", 2.1);

        mockMvc.perform(MockMvcRequestBuilders.post(categoryBaseUrl)
                .content(objectMapper.writeValueAsString(dto1))
                .contentType(MediaType.APPLICATION_JSON)
        );

        Category category = categoryRepository.findByTitleDefaultText("目录t1").get(0);
        String target = "{\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"目录t1\"\n" +
                "    },\n" +
                "    \"seq\": 2.1\n" +
                "}";
        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(category), false);
    }

    // 删除目录
    @Test
    public void deleteCategory() throws Exception {
        Assert.assertEquals(true, categoryRepository.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete(categoryBaseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
        );

        Assert.assertEquals(false, categoryRepository.findById(1).isPresent());
    }

    // 编辑目录
    @Test
    public void editCategory() throws Exception {
        Category category = categoryRepository.findById(1).get();

        String target = "{\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"目录1\"\n" +
                "    },\n" +
                "    \"seq\": 1.1\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(category), false);

        MainController.CategoryDTO dto = new MainController.CategoryDTO("目录1c", 1.11);

        mockMvc.perform(MockMvcRequestBuilders.put(categoryBaseUrl + "/{id}", 1)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        category = categoryRepository.findById(1).get();

        target = "{\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"目录1c\"\n" +
                "    },\n" +
                "    \"seq\": 1.11\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(category), false);
    }

    // 获取单个目录
    @Test
    public void getCategory() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(categoryBaseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        String target = "{\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"目录1\"\n" +
                "    },\n" +
                "    \"seq\": 1.1\n" +
                "}";

        JSONAssert.assertEquals(target, result.getResponse().getContentAsString(), false);
    }

    // 获取多个目录
    @Test
    public void getCategories() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(categoryBaseUrl + "?pageNum=1&pageSize=2")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        String target = "[{\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"目录3\"\n" +
                "    },\n" +
                "    \"seq\": 3.0\n" +
                "},\n" +
                "{\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"目录4\"\n" +
                "    },\n" +
                "    \"seq\": 4.0\n" +
                "}]";

        JSONAssert.assertEquals(target, JsonPath.parse(result.getResponse().getContentAsString()).read("$.content").toString(), false);
    }
    // end 目录

    // 题目
    // 添加题目
    @Test
    public void addQuestion1() throws Exception {
        MainController.QuestionDto questionDto = new MainController.QuestionDto(
                QuestionType.completion,
                "填空题t1",
                1.1,
                1,
                "1 == 1",
                "2 == 1"
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(questionBaseUrl)
                .content(objectMapper.writeValueAsString(questionDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Question question = questionRepository.findByTitleDefaultText("填空题t1").get(0);

        String target = "{\n" +
                "    \"questionType\": \"completion\",\n" +
                "    \"titleDefaultText\": \"选择题t1\",\n" +
                "    \"seq\": 1.1,\n" +
                "    \"categoryId\": 1,\n" +
                "    \"fitRule\": \"1 == 1\",\n" +
                "    \"validateRule\": \"2 == 2\",\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(question), false);
    }

//    @Test
//    public void addQuestion2() throws Exception {
//        MainController.QuestionDto questionDto = new MainController.QuestionDto(
//                QuestionType.completion,
//                "选择题t1",
//                1.1,
//                1,
//                "1 == 1",
//                "2 == 1"
//        );
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(questionBaseUrl)
//                .content(objectMapper.writeValueAsString(questionDto))
//                .contentType(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//        Question question = questionRepository.findByTitleDefaultText("复合题t1").get(0);
//
//        String target = "{\n" +
//                "    \"questionType\": \"compound\",\n" +
//                "    \"titleDefaultText\": \"复合题t1\",\n" +
//                "    \"seq\": 1.1,\n" +
//                "    \"categoryId\": 1,\n" +
//                "    \"fitRule\": \"1 == 1\",\n" +
//                "    \"validateRule\": \"2 == 2\",\n" +
//                "    \"minNum\": 1,\n" +
//                "    \"maxNum\": 5,\n" +
//                "    \"questions\": [\n" +
//                "        {\n" +
//                "            \"questionType\": \"completion\",\n" +
//                "            \"titleDefaultText\": \"子填空题t1_1\",\n" +
//                "            \"seq\": 1.1,\n" +
//                "            \"validateRule\": \"2 == 2\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"questionType\": \"judgement\",\n" +
//                "            \"titleDefaultText\": \"子判断题t1_2\",\n" +
//                "            \"seq\": 1.2,\n" +
//                "            \"validateRule\": \"2 == 2\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"questionType\": \"choice\",\n" +
//                "            \"titleDefaultText\": \"子选择题t1_3\",\n" +
//                "            \"seq\": 1.3,\n" +
//                "            \"validateRule\": \"2 == 2\",\n" +
//                "            \"options\": [\n" +
//                "                {\n" +
//                "                    \"key\": 1,\n" +
//                "                    \"value\": {\n" +
//                "                        \"defaultText\": \"子问题选项1\"\n" +
//                "                    },\n" +
//                "                    \"score\": 1\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"key\": 2,\n" +
//                "                    \"value\": {\n" +
//                "                        \"defaultText\": \"子问题选项2\"\n" +
//                "                    },\n" +
//                "                    \"score\": 2\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
//
//        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(question), false);
//    }
    // 删除题目
    // 编辑题目
    // 获取单个题目
    // 获取多个题目
    // end 题目

    // 获取入职试卷

}
