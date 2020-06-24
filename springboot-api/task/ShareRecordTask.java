package com.eworldlink.api.task;

import com.eworldlink.api.online.biz.ShareRecordBiz;
import com.eworldlink.api.online.entity.ShareRecordEntity;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Log4j
public class ShareRecordTask {

    @Autowired
    private ShareRecordBiz shareRecordBiz;

    //private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(fixedRate = 5000*12)
    public void updateUserMobile() {
        //log.info("开始:" + sdf.format(new Date()));
        List<ShareRecordEntity> list = shareRecordBiz.selectAllUserMobileAndOpenid();

        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ShareRecordEntity entity = list.get(i);
                shareRecordBiz.updateUserRecordInfo(entity);
            }
        }
        //log.info("结束:" + sdf.format(new Date()));
    }
}
