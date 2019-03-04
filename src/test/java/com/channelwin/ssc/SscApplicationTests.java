package com.channelwin.ssc;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SscApplicationTests {

//    @Test
//    public void getJSONOfMultiLang() {
//        MultiLang multiLang = new MultiLang("defaultText1");
//        multiLang.setText(Lang.PY, "defaultText1 py");
//        multiLang.setText(Lang.EN, "defaultText1 en");
//
//        JSONObject jsonObject = multiLang.getJSON();
//
////        log.info(JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat));
//
//        String expected = "{\n" +
//                "\t\"id\":0,\n" +
//                "\t\"translation\":{\n" +
//                "\t\t\"PY\":\"defaultText1 py\",\n" +
//                "\t\t\"EN\":\"defaultText1 en\"\n" +
//                "\t},\n" +
//                "\t\"defaultText\":\"defaultText1\"\n" +
//                "}\n";
//        JSONAssert.assertEquals(jsonObject.toJSONString(), expected, true);
//    }
//
//    @Test
//    public void getJSONOfCategory() throws JSONException {
//        Category category = new Category("目录1", 0);
//
//        Question cq1 = new CompletionQuestion("复合填空1");
//        Question cq2 = new JudgementQuestion("复合判断1");
//        Question cq3 = new ChoiceQuestion("复合选择1", "复合选择1选项1", "复合选择1选项2");
//
//        CompoundQuestion q1 = new CompoundQuestion("复合题1", category, cq1, cq2, cq3);
//        q1.setMaxNum(5);
//        q1.setMinNum(3);
//
//        log.info(JSON.toJSONString(q1.getJSON(), SerializerFeature.PrettyFormat));
//    }


}
