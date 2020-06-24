package com.eworldlink.api.customerManage.controller;

import com.eworldlink.api.config.controller.BaseController;
import com.eworldlink.api.customerManage.biz.CustomerManageBiz;
import com.eworldlink.api.customerManage.entity.CustomerManageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/customerManage")
public class CustomerManageController extends BaseController<CustomerManageBiz, CustomerManageEntity> {

    @RequestMapping(value="/outline",method = RequestMethod.POST)
    @ResponseBody
    public List<CustomerManageEntity> outline(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        return baseBiz.selectDepartOrStateOrStartOrEnd(data);
    }
    // 为什么
    @RequestMapping(value="/search",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> outlineSearch(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        return baseBiz.selectOutlineSearch(data);
    }
//    // 有效客户明细表
//    @RequestMapping(value="/validClient",method = RequestMethod.POST)
//    @ResponseBody
//    public List<CustomerManageEntity> validClient(@RequestBody Map<String, Object> data) {
//        System.out.println(data);
//        return baseBiz.selectValidClientInfo(data);
//    }

    // 统计数据带看次数和电话次数
    @RequestMapping(value="/validClientCompute",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> validClientCompute(@RequestBody Map<String, Object> data) {
        System.out.println("收到的参数:" + data);
        return baseBiz.selectValidClientCompute(data);
    }
    /**
     * 统计招商人员在某一时间段的客户人数的排名
     */
    @RequestMapping(value="/rank",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> clientCountRank(@RequestBody Map<String, Object> data) {
        System.out.println("rank data:" + data);
        return baseBiz.selectClientCountRank(data);
    }
    /**
     * 统计六大部门各自的数据
     */
    @RequestMapping(value="/statis",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> statisDepart(@RequestBody Map<String, Object> data) {
        System.out.println(data);
        return baseBiz.selectStatisDepart(data);
    }

    /**
     * home页面上的行业 活跃客户数 需求面积 的统计
     * 筛选条件 行业 时间30天以内 客户状态 面积 业务员名字 部门
     * /customerManage/homeStatis
     */
    @RequestMapping(value = "/homeStatis", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> homeStatis(@RequestBody Map<String, Object> data){
        return baseBiz.homeStatisBiz(data);
    }
}
