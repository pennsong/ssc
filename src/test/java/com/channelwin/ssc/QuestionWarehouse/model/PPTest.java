package com.channelwin.ssc.QuestionWarehouse.model;

import com.channelwin.ssc.QuestionWarehouse.repository.MultiLangRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class PPTest {
    @Autowired
    private static MultiLangRepository multiLangRepository;

    @BeforeClass
    public static void setup() {
        log.info("setup start");
        multiLangRepository.save(FactoryService.createMuliLang("testML"));
        log.info("setup end");
    }

    @Test
    public void test1() {
        log.info("test1 start");

        try {
            Thread.sleep(1000);
            long count = StreamSupport.stream(multiLangRepository.findAll().spliterator(), false).count();

            log.info("test1 count:" + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("test1 end ");
    }

    @Test
    public void test2() {
        log.info("test1 start");

        try {
            Thread.sleep(1000);

            long count = StreamSupport.stream(multiLangRepository.findAll().spliterator(), false).count();

            log.info("test2 count:" + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("test2 end ");
    }

    @Test
    public void test3() {
        log.info("test1 start");

        try {
            Thread.sleep(1000);

            long count = StreamSupport.stream(multiLangRepository.findAll().spliterator(), false).count();

            log.info("test3 count:" + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("test3 end ");
    }

}
