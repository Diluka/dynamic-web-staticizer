package com.github.diluka.dynamic.web.staticizer.config;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


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
