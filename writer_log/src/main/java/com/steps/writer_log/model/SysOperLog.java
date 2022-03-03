package com.steps.writer_log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fx
 * @date 2021-08-26 15:45
 */

@TableName("sysoperlog")
@Data
@ToString
public class SysOperLog implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String operModel;

    private String operUrl;

    private String requestMethod;

    private Integer operType;

    private String operParam;

    private String jsonResult;

    private String orgCode;

    private String operName;

    private String operIp;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operDate;

    private Long consumingTime;

    private Integer status;

    private String errorMsg;

    private String remark;
}
