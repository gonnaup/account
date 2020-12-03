package org.gonnaup.accountmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;


@SpringBootApplication
@MapperScan(value = "org.gonnaup.accountmanagement.dao", annotationClass = Repository.class)
@Controller
public class AccountManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountManagementApplication.class, args);
    }
}
