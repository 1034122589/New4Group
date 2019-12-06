package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClearOrderSettingJob {

    @Reference
    OrderSettingService orderSettingService;

    //定时清理预约设置历史数据
    public void ClearOrderSetting(){
        try {
            //获得定时任务执行日期(保存一年的数据)
            //Calendar calendar = Calendar.getInstance();
            //calendar.add(Calendar.MONTH,-3);// -12表示根据当前时间，将Calendar对象向前推12个月（从2018-12）
            //String oneYearsAgo = DateUtils.parseDate2String(calendar.getTime());
            Date date = DateUtils.getToday();
            String date2String = new SimpleDateFormat("yyyy-MM-dd").format(date);
            orderSettingService.ClearOrderSettingByDate(date2String);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
