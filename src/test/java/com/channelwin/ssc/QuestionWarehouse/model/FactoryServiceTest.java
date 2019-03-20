package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.repository.CategoryRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
public class FactoryServiceTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private FactoryService factoryService;

    private ObjectMapper objectMapper = new ObjectMapper()
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .enable(SerializationFeature.INDENT_OUTPUT);;

    private String categoryTitleDefaultText = "目录t1";

    private Category category;

    @Before
    public void setUp() {
        log.info("setUp");
        Map<Lang, String> map = new HashMap<>();
        map.put(Lang.PY, "目录t1 PY");
        map.put(Lang.EN, "目录t1 EN");
        MultiLang lang = new MultiLang(categoryTitleDefaultText, map);
        category = new Category(lang, 1.0);
        Mockito.when(categoryRepository.findById(1)).thenReturn(java.util.Optional.of(category));
        Mockito.when(categoryRepository.findById(2)).thenReturn(null);
    }

    @Test
    public void createMuliLang() throws JsonProcessingException, JSONException {
        String titleDefaultText = "mt1";
        MultiLang lang = factoryService.createMuliLang(titleDefaultText);

        String target = "{" +
                "defaultText: " + titleDefaultText + "," +
                "translation: {}" +
                "}";
        JSONAssert.assertEquals(target, objectMapper.writeValueAsString(lang), false);
    }

    @Test
    public void createMuliLang2() throws JsonProcessingException, JSONException {
        String titleDefaultText = "";
        MultiLang lang = factoryService.createMuliLang(titleDefaultText);

        Set<ConstraintViolation<MultiLang>> violations = validator.validate(lang);
        assertEquals(1, violations.size());
    }

}
