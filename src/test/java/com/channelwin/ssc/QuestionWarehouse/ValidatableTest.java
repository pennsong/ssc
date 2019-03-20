package com.channelwin.ssc.QuestionWarehouse;

import com.channelwin.ssc.Validatable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
public class ValidatableTest {

    @Data
    @NoArgsConstructor
    class V extends Validatable {
        int intV;

        String str;

        List<V> list;

        Set<V> set;

        Map<V, V> map;

        V[] vs;

        V v;

        public V(int i, String str) {
            this.intV = i;
            this.str = str;
        }

        @Override
        public void validate() {
            // 父类校验
            super.validate();

            // 单项校验
            Validation.buildDefaultValidatorFactory().getValidator()
                    .validate(this);

            // 单项Validatable校验, ValidateIgnore的忽略
            Validatable.validateObjectValidatableFields(this);

            // 整体校验
            log.info(this.toString() + ":整体校验");
        }
    }

    @Test
    public void test() {
        V v = new V();
        v.setIntV(1);
        v.setStr("root");
        v.setList(Arrays.asList(new V(11, "l11"), new V(12, "l12")));
        v.setSet(new HashSet<V>(Arrays.asList(new V(21, "l21"), new V(22, "l22"))));
        HashMap<V, V> map = new HashMap<>();
        map.put(new V(31, "l31"), new V(32, "l32"));
        map.put(new V(33, "l33"), new V(34, "l34"));
        v.setMap(map);
        v.setVs(new V[]{new V(41, "l41"), new V(42, "l42")});
        v.setV(new V(51, "l51"));

        log.info("pptest begin");
        v.validate();
        log.info("pptest end");
    }
}
