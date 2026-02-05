package org.codegym.springbootdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Pointcut("within(org.codegym.springbootdemo.web.*Controller)")
  public void controllerPointcut() {}

  @Pointcut("within(org.codegym.springbootdemo.service.*Service)")
  public void servicePointcut() {}

  @Pointcut("within(org.codegym.springbootdemo.repository.*Repository)")
  public void repositoryPointcut() {}

  @Before("controllerPointcut()")
  public void logBeforeController(JoinPoint joinPoint) {
    log.info("@Before controller:{}", joinPoint.getSignature().getName());
  }

  @AfterReturning(pointcut = "servicePointcut()", returning = "result")
  public void logAfterService(JoinPoint joinPoint, Object result) {
    log.info("@After service:{}, result {}", joinPoint.getSignature().getName(), result);
  }

  @AfterThrowing(pointcut = "servicePointcut()", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
    log.error("@AfterThrowing service:{}, ex: {}", joinPoint.getSignature().getName(), ex.getLocalizedMessage());
  }
}
