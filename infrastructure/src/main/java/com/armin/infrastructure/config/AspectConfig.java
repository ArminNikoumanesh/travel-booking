package com.armin.infrastructure.config;

import com.armin.security.authentication.filter.JwtUser;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.UserContextDto;
import com.armin.utility.repository.odm.dao.ElasticsearchLogDao;
import com.armin.utility.repository.odm.logs.entity.AuditLogElastic;
import com.armin.utility.repository.odm.logs.entity.ExceptionsLogElastic;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static com.armin.utility.object.ClientIpInfo.getMainIP;

/**
 * Aspect Oriented Log configuration
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

@Component
@Aspect
@Slf4j
public class AspectConfig {

    @Autowired
    private ElasticsearchLogDao elasticsearchLogDao;
    @Autowired
    private Environment environment;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice * )")
    public void controllerAdvice() {
        /*
        This method contains controller Advice
         */
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController * || @org.springframework.stereotype.Controller *)")
    public void controller() {
        /*
        This method contains all controller class methods
         */
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void service() {
        /*
        This method contains all service class methods
         */
    }

    @Pointcut("@annotation(org.springframework.cache.annotation.CacheEvict)")
    public void cacheEvict() {
        /*
        This method contains all cacheEvict methods
         */
    }

    @Pointcut("execution(public * *(..))")
    public void allMethod() {
        /*
        This method contains all methods
         */
    }

    @Before(value = "controller()")
    public void validate(JoinPoint joinPoint) throws SystemException {
        Object[] signatureArgs = joinPoint.getArgs();
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof IValidation) {
                ((IValidation) signatureArg).validate();
            }
        }
    }

    @Before(value = "controller() && !@annotation(com.armin.utility.object.NoLogging) " +
            "&& !@target(com.armin.utility.object.NoLogging) && args(..,request)", argNames = "point,request")
    public void auditLog(JoinPoint point, HttpServletRequest request) {
        AuditLogElastic auditLogElastic = new AuditLogElastic(point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), request, point.getArgs());
        Optional<UserContextDto> userContextDto = JwtUser.getPublicUser();
        userContextDto.ifPresent(auditLogElastic::setUserInfo);
        log.info(auditLogElastic.toString());
        elasticsearchLogDao.bulkIndexWithOutId(environment.getProperty("audit.elastic.index"), auditLogElastic);
    }

    @Before(value = "controller() && args(..,request)", argNames = "request")
    public void ipLog(HttpServletRequest request) {
        log.info("Request remote address : {}", getMainIP(request));
    }


    @AfterThrowing(pointcut = "(@annotation(com.armin.utility.object.LogAnnotation) || controller() || controllerAdvice()) && allMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        if (exception instanceof SystemException ||
                exception instanceof ConstraintViolationException ||
                exception instanceof AuthenticationException ||
                exception instanceof AccessDeniedException) {
            /*
                Ignored Log
             */
        } else {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();
            ExceptionsLogElastic exceptionsLogElastic = new ExceptionsLogElastic(joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), stackTrace, exception.toString());
            elasticsearchLogDao.bulkIndexWithOutId(environment.getProperty("exception.elastic.index"), exceptionsLogElastic);
        }
    }
}
