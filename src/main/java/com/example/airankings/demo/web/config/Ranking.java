package com.example.airankings.demo.web.config;

import lombok.Getter;
import lombok.Setter;

/*
* 返回页面排名的类
* */
@Getter
@Setter
public class Ranking {
    public String order;
    public String desc;

    public Ranking(String order, String desc) {
        this.order = order;
        this.desc = desc;
    }
}
