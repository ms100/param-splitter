package io.github.ms100.paramsplittersample;

import io.github.ms100.paramsplitter.annotation.EnableParamSplitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.Ordered;

/**
 * Hello world!
 *
 * @author zhumengshuai
 */
@SpringBootApplication
@EnableCaching(order = Ordered.LOWEST_PRECEDENCE - 1)
@EnableParamSplitter
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
