package work.lince.commons.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogInterceptor {

    @Autowired
    protected ObjectMapper objectMapper;

    @Around("@within(LogExecutionTime) || @annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
            long executionTime = System.currentTimeMillis() - start;
            if (executionTime < 1000) {
                log.info("[processName:{}][time:{}]Finish", joinPoint.getSignature().getName(), executionTime);
            } else {
                if (log.isTraceEnabled()) {
                    String args = objectMapper.writeValueAsString(joinPoint.getArgs());
                    log.trace("[processName:{}][time:{}]Finish, too slow! args:{}", joinPoint.getSignature().getName(), executionTime, args);
                } else {
                    log.warn("[processName:{}][time:{}]Finish, too slow!", joinPoint.getSignature().getName(), executionTime);
                }
            }
        }

    }

}