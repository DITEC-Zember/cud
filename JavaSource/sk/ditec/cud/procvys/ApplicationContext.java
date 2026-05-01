package sk.ditec.cud.procvys;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext {

    static Logger log = LoggerFactory.getLogger(ApplicationContext.class);
    private static ServletContext servletContext;

    public static void setServletContext(ServletContext context) {

        log.info("Zapametanie si aplikacneho kontextu s context path " + context.getContextPath());
        servletContext = context;
    }


    public static URL getResource(String path) throws MalformedURLException {
        return servletContext.getResource(path);
    }

    public static String getContextPath() {
        return servletContext.getContextPath();
    }
}