package com.barbazan.edadil.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Yaroslav S Malyshev (yaroslav.malyshev@gmail.com) 19.09.14
 */
public class GZIPFilter implements Filter {

    private static final String DEFAULT_CONTENT_TYPE_REGEXP = "text/html.*|text/xml.*|text/css.*";

    private static Logger logger = getLogger(GZIPFilter.class);

    private String contentTypeRegexp;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            String ae = request.getHeader("Accept-Encoding");
            if (ae != null && ae.contains("gzip")) {
                GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
                chain.doFilter(req, wrappedResponse);
                wrappedResponse.finishResponse();
            } else {
                chain.doFilter(req, res);
            }
        }
    }

    public void init(FilterConfig filterConfig) {
        this.contentTypeRegexp = StringUtils.defaultString(filterConfig.getInitParameter("contentTypeRegexp"), DEFAULT_CONTENT_TYPE_REGEXP);
        logger.info("Initialized with contentTypeRegexp=" + contentTypeRegexp);
    }

    public void destroy() {
    }


    public class GZIPResponseWrapper extends HttpServletResponseWrapper {

        protected HttpServletResponse origResponse = null;
        protected ServletOutputStream stream = null;
        protected PrintWriter writer = null;

        public GZIPResponseWrapper(HttpServletResponse response) {
            super(response);
            origResponse = response;
        }

        public void setContentType(String s) {
            super.setContentType(s);
        }

        public ServletOutputStream createOutputStream() throws IOException {
            String contentType = StringUtils.defaultString(getContentType()).trim().toLowerCase();
            boolean gzip = contentType.matches(contentTypeRegexp);
            if (gzip) {
                return (new GZIPResponseStream(origResponse));
            } else {
                return origResponse.getOutputStream();
            }
        }

        public void finishResponse() {
            try {
                if (writer != null) {
                    writer.close();
                } else if (stream != null) {
                    stream.close();
                }
            } catch (Throwable e) {
                /*do nothing*/
            }
        }

        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if(stream != null) {
                stream.flush();
            }
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called!");
            }
            if (stream == null) {
                stream = createOutputStream();
            }
            return stream;
        }

        public PrintWriter getWriter() throws IOException {
            if (writer != null) {
                return (writer);
            }
            if (stream != null) {
                throw new IllegalStateException("getOutputStream() has already been called!");
            }
            stream = createOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(stream, origResponse.getCharacterEncoding()));
            return writer;
        }

        public void setContentLength(int length) {
        }

    }

    public static class GZIPResponseStream extends ServletOutputStream {

        protected ByteArrayOutputStream baos = null;
        protected GZIPOutputStream gzipstream = null;
        protected boolean closed = false;
        protected HttpServletResponse response = null;
        protected ServletOutputStream output = null;

        private long reallyWrittenBytes = 0;

        public GZIPResponseStream(HttpServletResponse response) throws IOException {
            super();
            closed = false;
            this.response = response;
            this.output = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            gzipstream = new GZIPOutputStream(baos) {
                {
                    def.setLevel(Deflater.BEST_COMPRESSION);
                }
            };
        }

        public void close() throws IOException {
            if (closed) {
                throw new IOException("This output stream has already been closed");
            }
            gzipstream.finish();
            if (reallyWrittenBytes > 0) {
                byte[] bytes = baos.toByteArray();
                response.addHeader("Content-Length", Integer.toString(bytes.length));
                response.addHeader("Content-Encoding", "gzip");
                output.write(bytes);
                output.flush();
            }
            output.close();
            closed = true;
        }

        public void flush() throws IOException {
            if (closed) {
                throw new IOException("Cannot flush a closed output stream");
            }
            gzipstream.flush();
        }

        public void write(int b) throws IOException {
            if (closed) {
                throw new IOException("Cannot write to a closed output stream");
            }
            gzipstream.write((byte) b);
            reallyWrittenBytes++;
        }

        public void write(byte b[]) throws IOException {
            write(b, 0, b.length);
        }

        public void write(byte b[], int off, int len) throws IOException {
            if (closed) {
                throw new IOException("Cannot write to a closed output stream");
            }
            gzipstream.write(b, off, len);
            reallyWrittenBytes += len;
        }

    }
}

