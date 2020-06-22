package com.eworldlink.api.customerManage.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tab_conclusion_permit")
@Data
public class CustomerManagePermitEntity {

    @Id // 定义主键
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "depart")
    String depart;

    @Column(name = "permit")
    String permit;
}
