package com.channelwin.ssc.QuestionWarehouse;

import com.channelwin.ssc.QuestionWarehouse.controller.MainController;
import com.channelwin.ssc.QuestionWarehouse.model.*;
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
        Category category = categoryRepository.findByTitleDefaultText("目录1").get(0);
        int id = category.getId();

        Assert.assertEquals(true, categoryRepository.findById(id).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete(categoryBaseUrl + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        );

        Assert.assertEquals(false, categoryRepository.findById(id).isPresent());
    }

    // 编辑目录
    @Test
    public void editCategory() throws Exception {
        Category category = categoryRepository.findByTitleDefaultText("目录1").get(0);
        int id = category.getId();

        MainController.CategoryEditDTO dto = new MainController.CategoryEditDTO(99.9);

        mockMvc.perform(MockMvcRequestBuilders.put(categoryBaseUrl + "/{id}", id)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        category = categoryRepository.findById(id).get();

        String target = "{\n" +
                "    \"seq\": 99.9\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(category), false);
    }

    // 获取单个目录
    @Test
    public void getCategory() throws Exception {
        Category category = categoryRepository.findByTitleDefaultText("目录1").get(0);
        int id = category.getId();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(categoryBaseUrl + "/{id}", id)
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
                new String[]{"2 == 1"}
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(questionBaseUrl)
                .content(objectMapper.writeValueAsString(questionDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Question question = questionRepository.findByTitleDefaultText("填空题t1").get(0);

        String target = "{\n" +
                "  \"title\" : {\n" +
                "    \"defaultText\" : \"填空题t1\"\n" +
                "  },\n" +
                "  \"seq\" : 1.1,\n" +
                "  \"questionType\" : \"completion\",\n" +
                "  \"category\" : {\n" +
                "    \"id\" : 1\n" +
                "  },\n" +
                "  \"compoundItem\" : false,\n" +
                "  \"fitRule\" : \"1 == 1\",\n" +
                "  \"validateRule\" : \"2 == 1\"\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(question), false);
    }

    @Test
    public void addQuestion2() throws Exception {

        MainController.QuestionDto completionQuestion1 = new MainController.QuestionDto(QuestionType.completion, "子填空题t1", 1.1,  new String[]{"2 == 1"});
        MainController.QuestionDto judgementQuestion1 = new MainController.QuestionDto(QuestionType.judgement, "子判断题t1", 1.2,  new String[]{"2 == 1"});
        MainController.QuestionDto choiceQuestion1 = new MainController.QuestionDto("子选择题t1",  new String[]{"2 == 1"}, 1.3, "子选择题t1_选项1", "子选择题t1_选项2");

        MainController.QuestionDto questionDto = new MainController.QuestionDto(
                "复合题t1",
                1.1,
                1,
                "1 == 1",
                new String[]{"2 == 1"},
                1,
                5,
                completionQuestion1,
                judgementQuestion1,
                choiceQuestion1
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(questionBaseUrl)
                .content(objectMapper.writeValueAsString(questionDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Question question = questionRepository.findByTitleDefaultText("复合题t1").get(0);

        String target = "{\n" +
                "    \"questionType\": \"compound\",\n" +
                "    \"title\": {\n" +
                "        \"defaultText\": \"复合题t1\" \n" +
                "    },\n" +
                "    \"seq\": 1.1,\n" +
                "    \"category\": {\n" +
                "        \"id\": 1\n" +
                "    },\n" +
                "    \"fitRule\": \"1 == 1\",\n" +
                "    \"validateRule\": \"2 == 2\",\n" +
                "    \"minNum\": 1,\n" +
                "    \"maxNum\": 5,\n" +
                "    \"questions\": [\n" +
                "        {\n" +
                "            \"questionType\": \"completion\",\n" +
                "            \"title\": {\n" +
                "                \"defaultText\": \"子填空题t1\" \n" +
                "            },\n" +
                "            \"seq\": 1.1,\n" +
                "            \"validateRule\": \"2 == 2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"questionType\": \"judgement\",\n" +
                "            \"title\": {\n" +
                "                \"defaultText\": \"子判断题t1\" \n" +
                "            },\n" +
                "            \"seq\": 1.2,\n" +
                "            \"validateRule\": \"2 == 2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"questionType\": \"choice\",\n" +
                "            \"title\": {\n" +
                "                \"defaultText\": \"子选择题t1\" \n" +
                "            },\n" +
                "            \"seq\": 1.3,\n" +
                "            \"validateRule\": \"2 == 2\",\n" +
                "            \"options\": [\n" +
                "                {\n" +
                "                    \"key\": 0,\n" +
                "                    \"value\": {\n" +
                "                        \"defaultText\": \"子选择题t1_选项1\"\n" +
                "                    },\n" +
                "                    \"score\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"key\": 1,\n" +
                "                    \"value\": {\n" +
                "                        \"defaultText\": \"子选择题t1_选项2\"\n" +
                "                    },\n" +
                "                    \"score\": 0\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(question), false);
    }

    // 删除题目
    @Test
    public void deleteQuestion() throws Exception {
        Question question = questionRepository.findByTitleDefaultText("填空题1").get(0);
        int id = question.getId();

        Assert.assertEquals(true, questionRepository.findById(id).isPresent());


        mockMvc.perform(MockMvcRequestBuilders.delete(questionBaseUrl + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        );

        Assert.assertEquals(false, questionRepository.findById(id).isPresent());
    }

    // 编辑题目
    @Test
    public void editQuestion() throws Exception {
        Question question = questionRepository.findByTitleDefaultText("填空题1").get(0);
        int id = question.getId();

        MainController.QuestionEditDto dto = new MainController.QuestionEditDto(8.8, 3, null);

        mockMvc.perform(MockMvcRequestBuilders.put(questionBaseUrl + "/{id}", id)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        question = questionRepository.findById(id).get();

        String target = "{\n" +
                "    \"id\": 41,\n" +
                "    \"seq\": 8.8,\n" +
                "    \"category\": {\n" +
                "        \"id\": 3\n" +
                "    }\n" +
                "}";

        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(question), false);
    }
    // 获取单个题目
    // 获取多个题目
    // end 题目

    // 获取入职试卷

}
