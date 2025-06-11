package sv.com.jsoft.efactmh.filter;

import javax.faces.application.ViewExpiredException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

@WebFilter(filterName = "AuthFilter", urlPatterns = "/app/*")
@Slf4j
public class AuthorizationFilter implements Filter {

    private FilterConfig config = null;
    private ServletContext servletContext = null;
    private static final String RUTA_INICIO = "/index.xhtml";
    private String[] noForwardsViewIds = null;
    private static final ArrayList<String> rutasContext = new ArrayList<>();

    public AuthorizationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        this.servletContext = config.getServletContext();
        this.initRutasContext();
    }

    public void initRutasContext() {
        String raiz = servletContext.getRealPath("/");
        String arrayDir[] = new File(raiz).list();
        for (String arrayDir1 : arrayDir) {
            boolean band = new File(raiz + "/" + arrayDir1).isDirectory();
            if (band) {
                rutasContext.add("/" + arrayDir1 + "/");
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            boolean isLogueado;
            isLogueado = AuthorizationFilter.checkLoginState(request, response);
            boolean checkExpire = AuthorizationFilter.checkExpiredPassState(request, response);
            String uri = req.getRequestURI();
            HttpServletRequest reqst = (HttpServletRequest) req;
            String pathInfo = reqst.getRequestURI().substring(reqst.getContextPath().length());

            if (isLogueado && (uri.indexOf("/login.xhtml") > 0 || pathInfo.equals("/"))) {
                res.sendRedirect(req.getContextPath() + "/index.xhtml");
            }
            if (checkExpire && uri.indexOf("/index.xhtml") > 0) {
                res.sendRedirect(req.getContextPath() + "/clave.xhtml");
            }
            if (isLogueado && (uri.contains("/procesos") || uri.contains("/mantenimientos"))) {
                res.sendRedirect(req.getContextPath() + "/index.xhtml");
            }
            if (isForwardable((HttpServletRequest) request) && !isLogueado) {
                String loginURI = this.getFilterConfig().getInitParameter("sv.com.jsoft.stdte.model.LoginURI");
                loginURI = (loginURI != null) ? loginURI : RUTA_INICIO;
                if (uri.indexOf("/public") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("/css") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("/images") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("/js") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("/lib") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("/validateToken") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("/updatePassword.xhtml") > 0) {
                    chain.doFilter(request, response);
                } else if (uri.indexOf("javax.faces.resource") > 0) {
                    chain.doFilter(request, response);
                } else if (isLogueado) {
                    chain.doFilter(request, response);
                } else if ("partial/ajax".equals(req.getHeader("Faces-Request"))) {
                    res.setContentType("text/xml");
                    res.getWriter()
                            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                            .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", req.getContextPath() + loginURI);
                } else {
                    res.sendRedirect(req.getContextPath() + loginURI);
                }
            } else {
                try {
                    chain.doFilter(request, response);
                } catch (IOException | ServletException et) {
                    log.error("ERROR EN doFilter", et);
                }
            }
        } catch (ServletException e) {
            if (e.getRootCause() instanceof ViewExpiredException) {
                res.sendRedirect(req.getContextPath() + "/index.xhtml");
            } else {
                log.info("SE PRESENTO EL SIGUIENTE ERROR doFilter: " + e);
            }
        }
    }

    public boolean isForwardable(HttpServletRequest request) {
        boolean onCallstack = true;
        boolean isNoForwardViewId = false;
        String noForwardViewId;
        String requestURI;
        Iterator noForwardViewIdIter;

        if (request.getAttribute("sv.com.sertracen.model.OnStack") == null) {
            request.setAttribute("sv.com.sertracen.model.OnStack", Boolean.TRUE);
            onCallstack = false;
        }

        requestURI = request.getRequestURI();
        noForwardViewIdIter = this.getNoForwardViewIds(request);

        while (!isNoForwardViewId && noForwardViewIdIter.hasNext()) {
            noForwardViewId = (String) noForwardViewIdIter.next();
            isNoForwardViewId = (requestURI.contains(noForwardViewId));
        }

        if (isNoForwardViewId) {
            return false;
        }

        return !onCallstack;
    }

    protected Iterator getNoForwardViewIds(HttpServletRequest request) {
        Iterator result;
        if (noForwardsViewIds == null) {
            synchronized (this) {
                noForwardsViewIds = new String[0];
                String viewIdList = this.getFilterConfig().getInitParameter("sv.com.jsoft.stdte.model.NoForwardViewIds");
                if (viewIdList != null) {
                    try {
                        noForwardsViewIds = viewIdList.split(" ");
                    } catch (Exception ex) {
                    }
                }
            }
        }

        result = Arrays.asList(noForwardsViewIds).iterator();
        return result;
    }

    public FilterConfig getFilterConfig() {
        return config;
    }

    private static boolean checkLoginState(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        boolean isLogueado = false;
        HttpSession session = ((HttpServletRequest) request).getSession(false);

        String login = null;
        if (session != null && (login = (String) session.getAttribute("username")) != null) {
            if (!login.isEmpty()) {
                isLogueado = true;
            }
        }
        return isLogueado;
    }

    public static boolean checkExpiredPassState(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        boolean statusPass = false;
        try {
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            String codigo = null;
            if (session != null && (codigo = (String) session.getAttribute("cmclv")) != null) {
                if (codigo.equals("28001")) {
                    statusPass = true;
                }
            }
        } catch (Exception e) {
            statusPass = false;
        }
        return statusPass;
    }
}
