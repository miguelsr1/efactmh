package sv.com.jsoft.efactmh.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import javax.faces.context.FacesContext;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

@WebServlet(name = "JasperReportServlet", urlPatterns = {"/viewReport/*"})
@Slf4j
public class JasperReportServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            String idFactura = request.getParameter("idFactura");
            
            JwtDto jwt = (JwtDto) request.getSession().getAttribute("jwt");
            ResponseRestApi rest = RestUtil.builder()
                    .clazz(String.class)
                    .endpoint("/api/secured/dte/report/pdf/" + idFactura)
                    .jwtDto(jwt)
                    .build()
                    .callGetOneAuth();

            if (rest.getCodeHttp() == 200) {
                String pdfBase64 = rest.getBody().toString();
                JsonObject jsonPdf = new Gson().fromJson(pdfBase64, JsonObject.class);
                byte[] byteRpt = Base64.getDecoder().decode(jsonPdf.get("pdf").getAsString());
                if (byteRpt != null) {
                    request.getSession().removeAttribute("REPORT");
                    response.setContentType("application/pdf");
                    response.setContentLength(byteRpt.length);
                    response.getOutputStream().write(byteRpt);

                }
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
