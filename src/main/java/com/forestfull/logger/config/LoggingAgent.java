package com.forestfull.logger.config;

import org.aspectj.weaver.loadtime.Agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class LoggingAgent {
    public static void premain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("[LoggingAgent] AspectJ 로드타임 위빙 활성화!");

        // AspectJ LTW 에이전트 실행
        Agent.premain(agentArgs, inst);
    }
}