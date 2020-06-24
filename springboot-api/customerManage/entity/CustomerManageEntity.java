package com.eworldlink.api.customerManage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tab_conclusion_table")
@Data
public class CustomerManageEntity {

    @Id // 定义主键
    String id;

    @Column(name = "datetime")  // 映射数据库中的字段
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    String dateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    String predictdealtime;

    @Column(name = "clientname")
    String clientName;

    @Column(name = "clientstyle")
    String clientStyle;

    @Column(name = "contactnumber")
    String contactNumber;

    @Column(name = "contactstaff")
    String contactStaff;

    @Column(name = "persontitle")
    String personTitle;

    // 客户状态 有效无效
    String clientstate;

    @Column(name = "sourcechannel")
    String sourceChannel;

    @Column(name = "phonevisit")
    String phoneVisit;

    @Column(name = "takevisit")
    String takeVisit;

    @Column(name = "needsquare")
    float needSquare;

    float price;

    @Column(name = "wantpark")
    String wantPark;

    @Column(name = "wantbuilding")
    String wantBuilding;


    @Column(name = "currentproblem")
    String currentproblem;

    @Column(name = "requiredsupport")
    String requiredsupport;

    @Column(name = "currentlevel")
    String currentLevel;

    @Column(name = "merchantstaff")
    String merchantStaff;

    @Column(name = "merchantdepartment")
    String merchantDepartment;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    String createtime;

    //新增字段 客户情况
    @Column(name = "clientinfo")
    String clientinfo;

    //新增字段 客户类型
    @Column(name = "clientindustry")
    String clientindustry;
}
