package com.eworldlink.api.customerManage.mapper;

import com.eworldlink.api.customerManage.entity.CustomerManageEntity;
import com.eworldlink.api.online.entity.BuildInfoEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 其实就是dao层继承了Mapper
 */
public interface CustomerManageMapper extends Mapper<CustomerManageEntity> {
    public List<CustomerManageEntity> selectDepartOrStateOrStartOrEnd(Map<String, Object> params);
    public List<Map<String, Object>> selectOutlineSearch(Map<String, Object> params);
    public List<CustomerManageEntity> selectValidClientInfo(Map<String, Object> params);
    public List<Map<String, Object>> selectValidClientCompute(Map<String, Object> params);
    public List<Map<String, Object>> selectClientCountRank(Map<String, Object> params);
    public List<Map<String, Object>> selectStatisDepart(Map<String, Object> params);
    public List<Map<String, Object>> homeStatisMapper(Map<String, Object> params);
    public int refreshClientState();
}
