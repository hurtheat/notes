package com.kyg.springbootdemoproperties.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author kyg
 * @version 1.0
 * @description
 * @since 2020/12/7 20:30
 */
@Data
@Component
public class Book {

    private String bookName;

    private String description;

}
