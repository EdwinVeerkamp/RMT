package de.flower.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author oblume
 */
public abstract class AbstractLoggingAspect {

//    @Pointcut("")
//    abstract protected void entry();

//    @Pointcut("cflow(entry())")
//    final void trace() {
//    }

    @Pointcut("")
    abstract protected void trace();

    @Before("trace()")
    public void _logEnter(JoinPoint.StaticPart jp) {
        logEnter(jp);
    }

    @After("trace()")
    public void _logExit(JoinPoint.StaticPart jp) {
        logExit(jp);
    }

    abstract protected void logEnter(JoinPoint.StaticPart jp);

    abstract protected void logExit(JoinPoint.StaticPart jp);

}
