package org.codegym.springbootdemo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

  @Around("within(org.codegym.springbootdemo.service.*Service)")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      Object result = joinPoint.proceed();
      long end = System.currentTimeMillis();
      log.info("PERF [{}#{}] completed in {} ms", joinPoint.getTarget().getClass().getSimpleName(),
          joinPoint.getSignature().getName(),
          end - start);
      return result;
    } catch (Throwable ex) {
      long end = System.currentTimeMillis();
      log.info("PERF [{}#{}] failed after {} ms with {}", joinPoint.getTarget().getClass().getSimpleName(),
          joinPoint.getSignature().getName(),
          end - start,
          ex.getLocalizedMessage());
      throw ex;
    }
  }
}
