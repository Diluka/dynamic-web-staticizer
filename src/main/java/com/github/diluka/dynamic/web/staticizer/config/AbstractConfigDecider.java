package com.github.diluka.dynamic.web.staticizer.config;

import javax.servlet.http.HttpServletRequest;

/**
 * 抽象Decider，自定义{@link #decide(HttpServletRequest)}方法继承此类
 *
 * @author Diluka
 */
public abstract class AbstractConfigDecider extends DefaultConfig implements IStaticizeDecider{
    public AbstractConfigDecider(String matcher, String param, String dir, String flag) {
        super(matcher, param, dir, flag);
    }

    public AbstractConfigDecider(IStaticizerConfig config){
        super(config.getURIPattern(), config.getParameterName(), config.getStaticPageDirectory(), config.getStaticFlag());
    }
}
