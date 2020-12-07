package com.kyg.springbootdemoproperties.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * @author kyg
 * @version 1.0
 * @description
 * @since 2020/12/7 20:28
 */
@Data
@ConfigurationProperties(prefix = "library")
public class Library {

    private String bookShelf;

    private String category;

    private List<Book> books;

}
