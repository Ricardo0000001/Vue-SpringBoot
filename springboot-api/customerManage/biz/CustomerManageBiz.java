package com.eworldlink.api.customerManage.biz;

import com.eworldlink.api.config.biz.BaseBiz;
import com.eworldlink.api.customerManage.entity.CustomerManageEntity;
import com.eworldlink.api.customerManage.mapper.CustomerManageMapper;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
//import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
@Transactional(rollbackFor = Exception.class)
public class CustomerManageBiz<M extends Mapper<T>, T> extends BaseBiz<CustomerManageMapper, CustomerManageEntity> {

    public List<CustomerManageEntity> selectDepartOrStateOrStartOrEnd(Map<String, Object> params) {
        return mapper.selectDepartOrStateOrStartOrEnd(params);
//        return new HashMap<String, Object>(){
//            {
//                put("list",mapper.selectDepartOrStateOrStartOrEnd(params));
//                put("status","200");
//            }
//        };
    }

    public int insertSelective(CustomerManageEntity entity) {
        System.out.println("print the entity's merchantStaff" + " "+entity);
        System.out.println("print the merchantStaff:" + entity.getMerchantStaff());
        if(entity.getMerchantStaff() == null){
            System.out.println("没有检测到员工姓名！");
            return 0;
       }
        return super.insertSelective(entity);
    }

    public List<Map<String, Object>> selectOutlineSearch(Map<String, Object> params){
        return mapper.selectOutlineSearch(params);
    }

    public List<CustomerManageEntity> selectValidClientInfo(Map<String, Object> params){
        return mapper.selectValidClientInfo(params);
    }

    public List<Map<String, Object>> selectValidClientCompute(Map<String, Object> params){
        System.out.println(mapper.selectValidClientCompute(params));
        return mapper.selectValidClientCompute(params);
    }

    /**
     * 排名
     * @param params
     * @return
     */
    public List<Map<String, Object>> selectClientCountRank(Map<String, Object> params){
        return mapper.selectClientCountRank(params);
    }
    /**
     * 部门数据统计
     */
    public List<Map<String,Object>> selectStatisDepart(Map<String,Object> params){
        return mapper.selectStatisDepart(params);
    }

    /**
     * home页面的统计
     * @param params
     * @return
     */
    public List<Map<String,Object>> homeStatisBiz(Map<String,Object> params){
        return mapper.homeStatisMapper(params);
    }


    /**
     * 每天晚上刷新数据库中三个月以上没更新的客户为不活跃
     */
    public int refreshClientState(){
        return mapper.refreshClientState();
    }
}
