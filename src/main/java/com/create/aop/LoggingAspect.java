/*
 * Copyright  2016 Sebastian Gil.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.create.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Aspect
public class LoggingAspect {
    private static final Logger LOG_PERF = LoggerFactory.getLogger("PERF");
    private static final Logger LOG_ALL = LoggerFactory.getLogger("ALL");
    private static final Logger LOG_EXC = LoggerFactory.getLogger("EXC");
    private static final Logger LOG_SVC = LoggerFactory.getLogger("SVC");
    private static final Logger LOG_RST = LoggerFactory.getLogger("RST");

    @Around("execution(* com.create.service.*.*(..))")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        final String watchId = getWatchId(joinPoint);
        final StopWatch stopWatch = new StopWatch(watchId);
        stopWatch.start(watchId);
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            LOG_PERF.trace(stopWatch.shortSummary());
        }
    }

    private String getWatchId(ProceedingJoinPoint joinPoint) {
        final String className = getClassName(joinPoint.getTarget());
        final String methodName = getMethodName(joinPoint.getSignature());
        return String.format("%s - %s", className, methodName);
    }

    @AfterThrowing(pointcut = "execution(* com.create..*.*(..)) " +
            "&& !execution(* com.create.aop.*.*(..)) " +
            "&& !execution(* com.create.application.configuration.*.*(..))",
            throwing = "exception")
    public void logException(Exception exception) {
        LOG_EXC.error("Exception", exception);
    }

    @Before("execution(* com.create..*.*(..)) " +
            "&& !execution(* com.create.aop.*.*(..)) " +
            "&& !execution(* com.create.application.configuration.*.*(..))")
    public void logAll(JoinPoint joinPoint) {
        debug(joinPoint, LOG_ALL);
    }

    @Before("execution(* com.create.service.*.*(..))")
    public void logService(JoinPoint joinPoint) {
        debug(joinPoint, LOG_SVC);
    }

    @Before("execution(* com.create.controller.*.*(..)) ")
    public void logController(JoinPoint joinPoint) {
        debug(joinPoint, LOG_RST);
    }

    private void debug(JoinPoint joinPoint,
                       Logger logger) {
        final String className = getClassName(joinPoint.getTarget());
        final String methodName = getMethodName(joinPoint.getSignature());
        final Object[] arguments = joinPoint.getArgs();
        logger.debug("{} - {}{}", className, methodName, arguments);
    }

    private String getClassName(Object target) {
        return target.getClass().getName();
    }

    private String getMethodName(Signature signature) {
        return signature.getName();
    }

}
