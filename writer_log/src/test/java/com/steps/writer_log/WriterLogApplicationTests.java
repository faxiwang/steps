package com.steps.writer_log;

import com.alibaba.fastjson.JSON;
import com.steps.writer_log.model.SysOperLog;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
class WriterLogApplicationTests {

	@Test
	void contextLoads() {

        try (BufferedReader reader = new BufferedReader(new FileReader("F:\\test.txt"))) {
            String line;
            while ((line = reader.readLine())!=null){
                SysOperLog t =  JSON.parseObject(line, SysOperLog.class);
                System.out.println(t.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
	}

}
