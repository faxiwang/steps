package com.steps.rocketmq.model;

import lombok.Data;

/**
 * @author fx
 * @date 2021-12-30 11:38
 */
@Data
public class ParkPayMessge {
    private Long id;

    private long dateTime;

    private String msg;
}
