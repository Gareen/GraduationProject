package cn.sams.aspect;


import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by Fanpeng on 2017/4/17.
 *
 */
public class LoggerAspect {

    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("start log:" + joinPoint.getSignature().getName());
        Object object = joinPoint.proceed();
        System.out.println("end log:" + joinPoint.getSignature().getName());
        return object;
    }
}
