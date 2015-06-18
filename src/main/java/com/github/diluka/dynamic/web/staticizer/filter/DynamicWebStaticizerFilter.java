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

import com.github.diluka.dynamic.web.staticizer.config.*;
import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 动态网页静态化器
 * <p>
 * 第一次请求一个动态页面时，会将其保存为html文件，之后请求不再动态生成
 *
 * @author Diluka
 */
public class DynamicWebStaticizerFilter implements Filter {

    private List<IStaticizeDecider> deciders = new ArrayList<IStaticizeDecider>();

    private final List<ICallback> callbacks = new ArrayList<ICallback>();

    private DefaultConfigDeciderFactory defaultConfigDeciderFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (defaultConfigDeciderFactory == null) {
            defaultConfigDeciderFactory = new DefaultConfigDeciderFactory();
        }

        deciders.addAll(defaultConfigDeciderFactory.getDefaultDeciders());

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Staticizer Initialized. ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        for (IStaticizeDecider decider : deciders) {
            if (request instanceof HttpServletRequest) {

                HttpServletRequest req = (HttpServletRequest) request;
                CacheHttpServletResponseWrapper chsrw = new CacheHttpServletResponseWrapper((HttpServletResponse) response);

                if (decider.decide(req)) {

                    File html = prepareStaticPage(decider, req);
                    if (html != null) {
                        response.setCharacterEncoding("utf-8");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(html), "utf-8"));

                        String s = reader.readLine();
                        while (s != null) {
                            response.getWriter().write(s);
                            s = reader.readLine();
                        }
                        reader.close();
                        return;
                    } else {
                        chain.doFilter(request, chsrw);
                        saveHtmlFile(decider, req, chsrw);
                        return;
                    }

                }
            }
        }

        chain.doFilter(request, response);

    }

    private void saveHtmlFile(IStaticizerConfig config, HttpServletRequest request, CacheHttpServletResponseWrapper response) throws FileNotFoundException, IOException {
        String filename = config.getFilename(request);
        File dir = getDir(config, request);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File html = new File(dir, filename);

        FileOutputStream fos = new FileOutputStream(html);
        fos.write(response.outputStream.toByteArray());

        fos.close();

        for (ICallback callback : callbacks) {
            callback.call(config, request);
        }
    }

    private File prepareStaticPage(IStaticizerConfig config, HttpServletRequest request) throws IOException {
        String filename = config.getFilename(request);
        File dir = getDir(config, request);
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

    private File getDir(IStaticizerConfig config, HttpServletRequest request) {
        String dir = config.getStaticPageDirectory();
        if (dir.contains(":")) {
            return new File(dir);
        } else {
            return new File(request.getServletContext().getRealPath("/"), dir);
        }
    }

    @Override
    public void destroy() {
        deciders = null;
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
        PrintWriter writer;

        public CacheHttpServletResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (writer == null) {
                writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), "utf-8"));
            }
            return writer;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new TeeServletOutputStream(super.getOutputStream(), outputStream);
        }

    }

    public List<IStaticizeDecider> getDeciders() {
        return deciders;
    }

    public void setDeciders(List<IStaticizeDecider> deciders) {
        this.deciders = deciders;
    }

    public void addDecider(IStaticizeDecider decider) {
        deciders.add(decider);
    }

    public void addConfig(IStaticizerConfig bean) {
        deciders.add(new DefaultConfigDecider(bean));
    }

    public void addCallback(ICallback callback) {
        callbacks.add(callback);
    }

    public void removeCallback(ICallback callback) {
        callbacks.remove(callback);
    }


}
