package sv.com.jsoft.efactmh.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import sv.com.jsoft.efactmh.db.DataSourceApp;

@WebServlet(name = "JasperReportServlet", urlPatterns = {"/viewReport/*"})
@Slf4j
public class JasperReportServlet extends HttpServlet {

    @Inject
    private DataSourceApp dataSourceApp;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long idFactura = (Long) request.getSession().getAttribute("idFactura");
            String codigoGeneracion = (String) request.getSession().getAttribute("codigoGeneracion");
            
            String pathReporte = System.getProperty("user.home").concat("/opt/efact/report/dte-efact-1.1.jasper");
            String pathImg = System.getProperty("user.home").concat("/opt/efact/report/images/logos/logo-dte.jpg");
            
            Map<String, Object> params = new HashMap<>();
            params.put("P_PATH_IMG", pathImg);
            params.put("PID_FACTURA", idFactura);

            try (Connection conn = dataSourceApp.getEfactDS().getConnection()) {
                byte[] byteRpt = JasperRunManager.runReportToPdf(pathReporte, params, conn);
                
                if (byteRpt != null) {
                    request.getSession().removeAttribute("REPORT");
                    response.setContentType("application/pdf");
                    response.setContentLength(byteRpt.length);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + codigoGeneracion + ".pdf\"");
                    response.getOutputStream().write(byteRpt);
                }
            } catch (SQLException | JRException e) {
                log.error("ERROR en JasperReportServlet: ", e);
            }

        } catch (IOException e) {
            log.error("ERROR en JasperReportServlet: ", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
