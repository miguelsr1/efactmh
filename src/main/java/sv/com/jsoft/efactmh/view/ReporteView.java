package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Base64;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@RequestScoped
public class ReporteView implements Serializable {

    private StreamedContent media;

    public ReporteView() {
        loadPdf();
    }

    private void loadPdf() {
        media = null;
        if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("idFactura")) {
            return;
        }

        try {
            Long idFactura = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idFactura");
            JwtDto jwt = (JwtDto) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("jwt");

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idFactura");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("jwt");

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

                    InputStream stream = new ByteArrayInputStream(byteRpt);

                    media = DefaultStreamedContent.builder()
                            .name("documento" + idFactura + ".pdf")
                            .contentType("application/pdf")
                            .stream(() -> stream)
                            .build();
                }
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idFactura");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("jwt");
        }
    }

    public StreamedContent getMedia() {
        return media;
    }
}
