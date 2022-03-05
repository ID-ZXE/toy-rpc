package com.github.test.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hangs.zhang
 * @date 2020/4/24 下午12:48
 * *********************
 * function:
 */
@Data
@NoArgsConstructor
public class Student {

    private Integer id;

    private String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
