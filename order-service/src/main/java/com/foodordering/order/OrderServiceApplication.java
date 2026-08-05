package com.foodordering.order;

import com.foodordering.order.model.User;
import com.foodordering.order.model.UserRole;
import com.foodordering.order.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDemoUsers(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByEmailIgnoreCase("customer@test.com")) {
                userRepository.save(new User(
                        "Demo Customer",
                        "customer@test.com",
                        "customer123",
                        UserRole.CUSTOMER
                ));
            }

            if (!userRepository.existsByEmailIgnoreCase("staff@test.com")) {
                userRepository.save(new User(
                        "Demo Staff",
                        "staff@test.com",
                        "staff123",
                        UserRole.STAFF
                ));
            }
        };
    }
}
