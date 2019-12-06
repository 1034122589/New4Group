package com.itheima.test;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

import java.util.Calendar;

public class DateUtisTest {
    @Test
    public void fun(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);// -12表示根据当前时间，将Calendar对象向前推12个月（从2018-12）
        //System.out.println(calendar.getTime());
        System.out.println( DateUtils.getToday());
    }
}
