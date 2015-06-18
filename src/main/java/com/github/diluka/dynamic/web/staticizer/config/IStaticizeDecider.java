package com.github.diluka.dynamic.web.staticizer.config;


import javax.servlet.http.HttpServletRequest;

public interface IStaticizeDecider extends IStaticizerConfig {
    boolean decide(HttpServletRequest request);
}
