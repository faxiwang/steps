package com.steps.writer_log.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.steps.writer_log.dao.LogMapper;
import com.steps.writer_log.model.SysOperLog;
import com.steps.writer_log.service.LogService;
import org.springframework.stereotype.Service;

/**
 * @date 2021-09-12 17:26
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, SysOperLog> implements LogService {

}
