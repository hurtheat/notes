package com.kyg.springbootdemoproperties.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author kyg
 * @version 1.0
 * @description
 * @since 2020/12/7 20:05
 */
@SpringBootTest
@EnableConfigurationProperties(Library.class)
class UserTest {

    @Autowired
    User user;

    @Autowired
    private Library library;

    @Autowired
    Website website;

    @Test
    void testValue() {
        System.out.println(user);
    }

    @Test
    void testProperties() {
        System.out.println(library);
    }

    @Test
    void testPropertySource() {
        System.out.println(website);
    }
}