package kr.co.polycube.backendtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BackendTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            BackendTestApplication.class,
            args
        );
    }

}
