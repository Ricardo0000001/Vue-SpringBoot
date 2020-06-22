package com.eworldlink.api.customerManage.mapper;

import com.eworldlink.api.customerManage.entity.CustomerManagePermitEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface CustomerManagePermitMapper extends Mapper<CustomerManagePermitEntity> {
    public List<CustomerManagePermitEntity> judgePermitMapper(Map<String, Object> params);
}
