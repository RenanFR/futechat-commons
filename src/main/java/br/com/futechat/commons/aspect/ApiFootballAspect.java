package br.com.futechat.commons.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.futechat.commons.exception.LeagueNotFoundException;
import br.com.futechat.commons.exception.PlayerNotFoundException;
import br.com.futechat.commons.exception.TeamNotFoundException;

@Aspect
@Component
public class ApiFootballAspect {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* br.com.futechat.commons.service.text.*.*(..))")
	private void whenToCall() {
	}

	@Around("whenToCall()")
	public Object handlePlayerNotFound(ProceedingJoinPoint joinPoint) throws Throwable {

		LOGGER.info("INTERCEPTANDO EXECUCAO DE: {}", joinPoint.getSignature().getName());
		try {
			return joinPoint.proceed();
		} catch (PlayerNotFoundException | TeamNotFoundException | LeagueNotFoundException e) {
			return e.getMessage();
		}
	}

}
