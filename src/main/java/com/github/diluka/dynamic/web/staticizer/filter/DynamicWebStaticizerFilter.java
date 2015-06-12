/*
 * Copyright 2015 Diluka.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.diluka.dynamic.web.staticizer.filter;

import com.github.diluka.dynamic.web.staticizer.config.DefaultConfigBeanFactory;
import com.github.diluka.dynamic.web.staticizer.config.IStaticizerConfigBean;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.io.output.TeeOutputStream;

/**
 * 动态网页静态化器
 *
 * 第一次请求一个动态页面时，会将其保存为html文件，之后请求不再动态生成
 *
 * @author Diluka
 */
public class DynamicWebStaticizerFilter implements Filter {

    private List<IStaticizerConfigBean> configs = new ArrayList<IStaticizerConfigBean>();

    private DefaultConfigBeanFactory defaultConfigBeanFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (defaultConfigBeanFactory == null) {
            defaultConfigBeanFactory = new DefaultConfigBeanFactory();
        }

        configs.addAll(defaultConfigBeanFactory.getDefaultConfigBeans());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        for (IStaticizerConfigBean config : configs) {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) request;
                CacheHttpServletResponseWrapper chsrw = new CacheHttpServletResponseWrapper((HttpServletResponse) response);

                if (Pattern.compile(config.getURIPattern()).matcher(req.getRequestURI()).find()) {
                    File html = prepareStaticPage(config, req);
                    if (html != null) {
                        BufferedReader reader = new BufferedReader(new FileReader(html));
                        String s = reader.readLine();
                        while (s != null) {
                            response.getWriter().write(s);
                            s = reader.readLine();
                        }
                        reader.close();
                        return;
                    } else {
                        chain.doFilter(request, chsrw);
                        saveHtmlFile(config, req, chsrw);
                        return;
                    }

                }
            }
        }

        chain.doFilter(request, response);

    }

    private void saveHtmlFile(IStaticizerConfigBean config, HttpServletRequest request, CacheHttpServletResponseWrapper response) throws FileNotFoundException, IOException {
        String filename = request.getParameter(config.getParameterName()) + ".html";
        File dir = new File(request.getServletContext().getRealPath("/"), config.getStaticPageDirectory());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File html = new File(dir, filename);

        FileOutputStream fos = new FileOutputStream(html);

        fos.write(response.outputStream.toByteArray());

        fos.close();
    }

    private File prepareStaticPage(IStaticizerConfigBean config, HttpServletRequest request) throws IOException {
        String filename = request.getParameter(config.getParameterName()) + ".html";
        File dir = new File(request.getServletContext().getRealPath("/"), config.getStaticPageDirectory());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File html = new File(dir, filename);

        if (html.exists()) {
            return html;
        } else {
            return null;
        }
    }

    @Override
    public void destroy() {
        configs = null;
    }

    static class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }
    }

    static class CacheHttpServletResponseWrapper extends HttpServletResponseWrapper {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        public CacheHttpServletResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new TeeServletOutputStream(super.getOutputStream(), outputStream);
        }

    }

    public void setConfigs(List<IStaticizerConfigBean> configs) {
        this.configs = configs;
    }

    public List<IStaticizerConfigBean> getConfigs() {
        return configs;
    }

    public void addConfig(IStaticizerConfigBean bean) {
        configs.add(bean);
    }

    public void setDefaultConfigBeanFactory(DefaultConfigBeanFactory defaultConfigBeanFactory) {
        this.defaultConfigBeanFactory = defaultConfigBeanFactory;
    }

}
