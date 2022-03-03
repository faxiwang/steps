package com.steps.writer_log.aop;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steps.writer_log.annotation.SysLog;
import com.steps.writer_log.model.SysOperLog;
import com.steps.writer_log.service.LogService;
import com.steps.writer_log.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Configuration
@Slf4j
public class ControllerInterceptor {
    @Autowired
    private LogService logService;

    private Long statrTime = 0L;

    private HttpServletRequest request;

    /**
     * 定义拦截规则：拦截com.xinyi.xinfo.controller包下面的所有类中，有@SysLog注解的方法。
     */
    @Pointcut("execution(* com.steps.writer_log.controller..*.*(..))  && @annotation(com.steps.writer_log.annotation.SysLog)")
    public void controllerMethodPointcut() {
    }

    /**
     * 拦截器具体实现
     *
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     * @throws Throwable
     */
    @Around("controllerMethodPointcut()")
    public Object interceptor(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;

        String description = getControllerMethodDescription(thisJoinPoint);

        if (sra != null) {
            HttpServletResponse response = sra.getResponse();

            if (response != null) {
                response.addHeader("operateName", URLEncoder.encode(description, "UTF-8"));
            }
        }

        // result的值就是被拦截方法的返回值
        Object result = null;
        try {
            result = thisJoinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    @Before("controllerMethodPointcut()")
    private void doBefore() {
        statrTime = System.currentTimeMillis();
    }

    /**
     * 处理完请求后执行 系统操作日志记录
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "controllerMethodPointcut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作（注：若异常被捕获则不会执行本方法）
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "controllerMethodPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    private void handleLog(JoinPoint joinPoint, Exception e, Object jsonResult) {
        try {
            // 获得注解
            SysLog sysLog = getAnnotationLog(joinPoint);
            // 是否需要记录日志
            if (sysLog == null) return;

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.error("请求为空，无法获取请求信息！");
                return;
            }
            request = attributes.getRequest();

            // 请求的地址
            String ip = IpUtils.getIpAddr(request);
            String url = request.getRequestURI();
            String method = request.getMethod();
            //请求协议
            // 返回数据
            String result = JSON.toJSONString(jsonResult);
            // 获取参数的信息
            Map<String, Object> params = getFieldsName(joinPoint);

            int status = 1;
            String errorMsg = "";
            if (e != null) {
                status = 0;
                errorMsg = e.getMessage();
            }
            //接口响应时间
            Long duration = System.currentTimeMillis() - statrTime;

            // 系统操作日志
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(status);
            operLog.setOperUrl(url);
            operLog.setOperIp(ip);
            operLog.setOperName("admin");
            operLog.setOrgCode("001");
            operLog.setOperDate(LocalDateTime.now());
            operLog.setJsonResult(result);
            operLog.setErrorMsg(errorMsg);
            operLog.setRequestMethod(method);
            operLog.setOperType(0);
            operLog.setRemark(sysLog.value());
            operLog.setConsumingTime(duration);
            operLog.setOperParam(JSON.toJSONString(params));


            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("F:\\test.txt",true)))) {
                writer.write(JSON.toJSONString(operLog)+"\r\n");
            }
            //logService.save(operLog);


        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==记录操作日志发生异常==");
            log.error(exp.getMessage(), exp);
        }
    }

    /**
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    private String getControllerMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String result = null;

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                SysLog sysLog = method.getAnnotation(SysLog.class);
                result = sysLog.value();
            }
        }
        return result;
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private SysLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        if (methodSignature != null) {
            Method method = methodSignature.getMethod();
            if (method != null) {
                return method.getAnnotation(SysLog.class);
            }
        }
        return null;
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof MultipartFile[] || o instanceof HttpServletRequest
                || o instanceof HttpServletResponse;
    }

    /**
     * 获取参数信息
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private Map<String, Object> getFieldsName(JoinPoint joinPoint) {
        // 参数值
        Object[] args = joinPoint.getArgs();
        // 参数名
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = method.getParameterNames();
        // 通过map封装参数和参数值
        HashMap<String, Object> paramMap = new HashMap();
        for (int i = 0; i < parameterNames.length; i++) {
            if (!isFilterObject(args[i])) {
                // page中包含返回的数据，单独处理
                if (args[i] instanceof Page) {
                    Page page = new Page();
                    BeanUtils.copyProperties(args[i], page);
                    page.setRecords(null);
                    page.setOrders(null);
                    paramMap.put(parameterNames[i], page);
                } else {
                    paramMap.put(parameterNames[i], args[i]);
                }
            }
        }
        return paramMap;
    }
}
