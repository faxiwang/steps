package com.steps.writer_log.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steps.writer_log.model.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @date 2021-09-12 17:04
 */
@Mapper
public interface LogMapper extends BaseMapper<SysOperLog> {
}
