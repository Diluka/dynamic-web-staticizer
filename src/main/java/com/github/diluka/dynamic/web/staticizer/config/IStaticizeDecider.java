package com.github.diluka.dynamic.web.staticizer.config;


import javax.servlet.http.HttpServletRequest;

/**
 * 判断器，是否静态化由{@link #decide}方法的返回值决定
 *
 * @author Diluka
 */
public interface IStaticizeDecider extends IStaticizerConfig {
    /**
     * 静态化条件判定
     * @param request HttpServletRequest
     * @return
     */
    boolean decide(HttpServletRequest request);
}
