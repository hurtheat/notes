package com.kyg.springbootdemoproperties.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author kyg
 * @version 1.0
 * @description
 * @since 2020/12/7 21:09
 */
@Component
@Data
@PropertySource("classpath:Website.yml")
public class Website {

    @Value("${url}")
    private String url;

}
