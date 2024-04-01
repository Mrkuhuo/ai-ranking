package com.example.airankings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableWebSocket
public class AiRankingsApplication {

    /*该函数是Spring Boot应用程序的入口点。它使用SpringApplication.run方法来启动和运行应用程序，将当前类AiRankingsApplication.class作为应用程序的主类，并将args作为命令行参数传递给应用程序。*/
    public static void main(String[] args) {
        SpringApplication.run(AiRankingsApplication.class, args);
    }

}
