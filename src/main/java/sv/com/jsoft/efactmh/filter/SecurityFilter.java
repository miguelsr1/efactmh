package sv.com.jsoft.efactmh.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.view.SessionView;

/**
 *
 * @author 
 */
/*@WebFilter("/ope/*")*/
@Slf4j
public class SecurityFilter /*implements Filter*/ {
    
    @Inject
    SessionView sessionView;

    //@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        validateSession(request, response, chain);
    }

    private void validateSession(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);

            if (session != null && session.getAttribute("loggedIn") != null) {
                String storedIp = (String) session.getAttribute("clientIpAddress");
                String currentIp = httpRequest.getRemoteAddr();
                String storedUserAgent = (String) session.getAttribute("clientUserAgent");
                String currentUserAgent = httpRequest.getHeader("User-Agent");

                if (!currentIp.equals(storedIp) || !currentUserAgent.equals(storedUserAgent)) {
                    try {
                        log.error("INVALIDANDO SESSION SOSPECHOSA");
                        log.error("---- REQUEST INFO ----");
                        log.error("Timestamp: " + LocalDateTime.now());
                        log.error("Session ID: " + session.getId());
                        log.error("Remote User (Authenticated): " + (httpRequest.getRemoteUser() != null ? httpRequest.getRemoteUser() : "N/A"));
                        log.error("Remote Address: " + httpRequest.getRemoteAddr() + ":" + httpRequest.getRemotePort());
                        log.error("Local Address (Server): " + httpRequest.getLocalAddr() + ":" + httpRequest.getLocalPort());
                        log.error("Method: " + httpRequest.getMethod());
                        log.error("Request URI: " + httpRequest.getRequestURI() + (httpRequest.getQueryString() != null ? "?" + httpRequest.getQueryString() : ""));
                        log.error("User-Agent: " + httpRequest.getHeader("User-Agent"));
                        log.error("Referer: " + httpRequest.getHeader("Referer"));
                        log.error("----------------------");
                    } catch (Exception e) {
                        log.error("ERROR EN RECUPERANDO INFORMACION DE SESSION SOSPECHOSA");
                    }

                    sessionView.logout();
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/landing.xhtml");
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (IOException | ServletException ex) {
            log.error("ERROR en validateSession", ex);
        }
    }

}
