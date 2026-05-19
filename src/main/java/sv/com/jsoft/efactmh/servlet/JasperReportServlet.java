package sv.com.jsoft.efactmh.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import sv.com.jsoft.efactmh.db.DataSourceApp;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.services.SessionService;

@WebServlet(name = "JasperReportServlet", urlPatterns = {"/viewReport/*"})
@Slf4j
public class JasperReportServlet extends HttpServlet {

    @Inject
    DataSourceApp dataSourceApp;
    @Inject
    SessionService sessionService;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            String pathLogo;
            String pathRelativo = System.getProperty("user.home") + File.separator + "report" + File.separator + "images" + File.separator + "logos" + File.separator ;

            Long idFactura = (Long) request.getSession().getAttribute("idFactura");

            Long idContribuyente = sessionService.getIdContribuyente();

            File fLogo = new File(pathRelativo + idContribuyente+ ".jpg");

            if(fLogo.exists()){
                pathLogo = pathRelativo + idContribuyente+ ".jpg";
            }else {
                pathLogo = pathRelativo + "logo-dte.jpg";
            }

            String codigoGeneracion = (String) request.getSession().getAttribute("codigoGeneracion");
            
            String pathReporte = System.getProperty("user.home").concat("/report/dte-efact-1.1.jasper");
            String pathImg = pathLogo;
            
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
