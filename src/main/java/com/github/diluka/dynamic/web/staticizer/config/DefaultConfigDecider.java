package com.github.diluka.dynamic.web.staticizer.config;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * 默认配置Decider
 * 判定方式和以前一样
 * @author Diluka
 */
public class DefaultConfigDecider extends AbstractConfigDecider {

    public DefaultConfigDecider(IStaticizerConfig config) {
        super(config);
    }

    public DefaultConfigDecider(String matcher, String param, String dir, String flag) {
        super(matcher, param, dir, flag);
    }


    @Override
    public boolean decide(HttpServletRequest request) {
        return (getStaticFlag() == null || getStaticFlag().isEmpty()
                ? true
                : request.getParameter(getStaticFlag()) != null)
                && Pattern.compile(getURIPattern())
                .matcher(request.getRequestURI().replaceFirst(request.getContextPath(), "")).find();
    }
}
