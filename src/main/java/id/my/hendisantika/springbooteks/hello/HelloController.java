package id.my.hendisantika.springbooteks.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-eks
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/01/26
 * Time: 07.00
 * To change this template use File | Settings | File Templates.
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot API is working ,testing docker 1! " + LocalDateTime.now();
    }


    @GetMapping("/")
    public String home() {
        return "Welcome! Your Spring Boot API is running 🚀 " + LocalDateTime.now();
    }
}
