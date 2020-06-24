package com.eworldlink.api.task;

import com.eworldlink.api.customerManage.biz.CustomerManageBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 每天晚上刷新数据库中 状态为有效的客户 变为不活跃
 */

@Component
public class RefreshClientState {
    @Autowired
    private CustomerManageBiz customerManageBiz;

    /**
     * 每天凌晨两点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "10 * * * * ?")
    public void refreshClientState(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制）
        System.out.println("execution time");
        customerManageBiz.refreshClientState();
    }

}
