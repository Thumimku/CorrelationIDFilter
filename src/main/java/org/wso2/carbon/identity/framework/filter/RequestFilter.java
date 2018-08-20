package org.wso2.carbon.identity.framework.filter;
/**
 * This RequestFilter class is used to filter http request which comes to wso2 IS.
 * For each request, new uuid (uniques id) will be created and added to request thread context via MDC.put().
 * <p>
 * When a micro service process the request, it logs certain information along with this uuid.
 * Flow of a particular request can be track by tracking this uuid.
 *
 * @see web.xml in <IS-HOME>/repository/conf/tomcat
 * --------------------------------------------------------------------------------------------
 * <filter>
 * <filter-name>RequestFilter</filter-name>
 * <filter-class>org.wso2.carbon.identity.framework.filter.RequestFilter</filter-class>
 * </filter>
 * <p>
 * <filter-mapping>
 * <filter-name>RequestFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * --------------------------------------------------------------------------------------------
 * Above xml code is use to declare and configure servlet filter.
 * filter-name  :- Class name.
 * filter-class :- Full class path.
 * filter-name  :- Class name.
 * url-pattern  :- All requests
 * @see log4j.properties in <IS-HOME>/repository/conf
 * --------------------------------------------------------------------------------------------------------------------
 * log4j.appender.CARBON_CONSOLE.layout.ConversionPattern=[%d] %P%5p {%c} [%X{CorrelationID}]- %x %m%n
 * Congiure log pattern for wso2 console (Terminal).
 * <p>
 * log4j.appender.CARBON_LOGFILE.layout.ConversionPattern=TID: [%T] [%S] [%d] %P%5p {%c} [%X{CorrelationID}] - %x %m %n
 * Configure log pattern for wso2carbon.log file.
 * --------------------------------------------------------------------------------------------------------------------
 */

import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RequestFilter implements Filter {

    // Key for MDC value
    private final static String correlationId = "CorrelationID";

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Create random unique id.
        UUID uuid = UUID.randomUUID();

        // Put the value into request thread context
        MDC.put(correlationId, uuid.toString());
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // Remove the value from the thread context inorder to add new value for following thread.
            MDC.remove(correlationId);

        }


    }

    public void destroy() {

    }
}
