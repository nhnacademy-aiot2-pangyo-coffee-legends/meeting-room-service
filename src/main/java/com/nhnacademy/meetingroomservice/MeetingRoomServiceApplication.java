package com.nhnacademy.meetingroomservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MeetingRoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingRoomServiceApplication.class, args);
    }

}
