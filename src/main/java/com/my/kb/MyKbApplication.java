package com.my.kb;

import lombok.SneakyThrows;
import nl.renarj.jasdb.core.SimpleKernel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PreDestroy;

@SpringBootApplication
//@EnableAutoConfiguration
//@EnableAsync
//@EnableMongoRepositories(value = "com.apple.kb.payment.repository")
public class MyKbApplication {
    private static SpringApplicationBuilder builder;

    public static void main(String[] args) {
        try {
            builder = new SpringApplicationBuilder(MyKbApplication.class);
            builder.run(args);
        } catch (Exception e) {
            if (builder != null && builder.context() != null) {
                builder.context().close();
            }
        }
    }

    @SneakyThrows
    @PreDestroy
    public void shutdownDB() {
        SimpleKernel.shutdown();
    }
}
