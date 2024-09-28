package kr.co.polycube.backendtest.AOP;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* kr.co.polycube.backendtest.Controller.UserController.*(..))")
    public void logClientAgent(JoinPoint joinPoint) {
        try {
            //http요청과 테스트코드에서 aop활성화
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String clientAgent = request.getHeader("User-Agent");
            System.out.println("Client Agent: " + (clientAgent != null ? clientAgent : "Not available in test environment"));
        } catch (IllegalStateException e) {
            System.out.println("Client Agent: Not available in test environment");
        }
    }
}
