package com.itheima.health.job;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test01 {
    @Test
    public void fun(){
        Date today = DateUtils.getToday();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(today);
        System.out.println(date);
    }
}
