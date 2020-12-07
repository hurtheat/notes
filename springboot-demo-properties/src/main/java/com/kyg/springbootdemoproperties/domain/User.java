package com.kyg.springbootdemoproperties.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author kyg
 * @version 1.0
 * @description
 * @since 2020/12/7 20:03
 */
@Data
@Component
public class User {

    @Value("${user.name1}")
    private String name;

    @Value("${user.password}")
    private String password;

}
