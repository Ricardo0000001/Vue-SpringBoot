package com.eworldlink.api.customerManage.controller;

import com.eworldlink.api.config.controller.BaseController;
import com.eworldlink.api.customerManage.biz.CustomerManagePermitBiz;
import com.eworldlink.api.customerManage.entity.CustomerManagePermitEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customerManagePermit")
public class CustomerManagePermitController extends BaseController<CustomerManagePermitBiz, CustomerManagePermitEntity> {
    /**
     * 权限分配
     */
    @RequestMapping(value="/outlinePermit",method = RequestMethod.POST)
    @ResponseBody
    public List<CustomerManagePermitEntity> judgePermitController(@RequestBody Map<String, Object> data) {
        System.out.println("print the get param:" + data);
        return baseBiz.judgePermitBiz(data);
    }
}
