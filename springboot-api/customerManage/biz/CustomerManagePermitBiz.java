package com.eworldlink.api.customerManage.biz;

import com.eworldlink.api.config.biz.BaseBiz;
import com.eworldlink.api.customerManage.entity.CustomerManagePermitEntity;
import com.eworldlink.api.customerManage.mapper.CustomerManagePermitMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Log4j
@Transactional(rollbackFor = Exception.class)
public class CustomerManagePermitBiz extends BaseBiz<CustomerManagePermitMapper, CustomerManagePermitEntity> {
    /**
     * 权限分配
     */
    public List<CustomerManagePermitEntity> judgePermitBiz(Map<String,Object> params){
        return mapper.judgePermitMapper(params);
    }
}
