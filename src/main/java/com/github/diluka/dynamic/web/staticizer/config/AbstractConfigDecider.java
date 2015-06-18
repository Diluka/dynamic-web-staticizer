package com.github.diluka.dynamic.web.staticizer.config;


public abstract class AbstractConfigDecider extends DefaultConfig implements IStaticizeDecider{
    public AbstractConfigDecider(String matcher, String param, String dir, String flag) {
        super(matcher, param, dir, flag);
    }

    public AbstractConfigDecider(IStaticizerConfig config){
        super(config.getURIPattern(), config.getParameterName(), config.getStaticPageDirectory(), config.getStaticFlag());
    }
}
