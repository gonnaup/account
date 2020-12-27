package org.gonnaup.accountmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@SpringBootApplication
@MapperScan(value = "org.gonnaup.accountmanagement.dao", annotationClass = Repository.class)
@Controller
@EnableCaching
public class AccountManagementApplication {

    @GetMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/index.html");
    }

    public static void main(String[] args) {
        SpringApplication.run(AccountManagementApplication.class, args);
    }
}
