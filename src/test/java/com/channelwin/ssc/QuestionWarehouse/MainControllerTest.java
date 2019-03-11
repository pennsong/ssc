package com.channelwin.ssc.QuestionWarehouse;

import com.channelwin.ssc.EntryMaterialCollecting.EmployeeFixItem;
import com.channelwin.ssc.QuestionWarehouse.model.*;
import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import com.channelwin.ssc.QuestionWarehouse.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.channelwin.ssc.Gender.MALE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MultiLangRepository multiLangRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private QuestionRepository questionRepository;

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

    @Test
    public void getEntryPaper() throws Exception {
        List questionList = Arrays.asList(
                completionQuestion1,
                judgementQuestion1,
                choiceQuestion1,
                compoundQuestion1
        );

        when(questionRepository.findAll()).thenReturn(questionList);

        EmployeeFixItem employeeFixItem = new EmployeeFixItem("idCardNo1", "姓名1", MALE);

//        mockMvc.perform(get("questionWarehouse/getEntryPaper", employeeFixItem))
//                .andDo(print());

        MvcResult mvcResult = mockMvc.perform(get("/questionWarehouse/getEntryPaper", employeeFixItem)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        log.info(content);
    }
}
