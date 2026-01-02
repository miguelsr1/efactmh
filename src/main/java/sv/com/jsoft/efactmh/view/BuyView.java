package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.services.BuyService;
import sv.com.jsoft.efactmh.services.SessionService;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ERROR;
import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;

/**
 *
 * @author msanchez
 */
@Named
@ViewScoped
@Slf4j
public class BuyView implements Serializable {

    @Getter
    @Setter
    private LocalDate buyDate;
    @Getter
    @Setter
    private UploadedFile file;

    @Getter
    private String codigoGeneracion;
    @Getter
    private String fechEmi;
    @Getter
    private String nitEmisor;
    @Getter
    private String nombreEmisor;
    @Getter
    private BigDecimal monto;

    @Inject
    SessionService securityService;
    @Inject
    BuyService buyService;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private JsonObject jsonObject;

    public void handleFileUpload(FileUploadEvent event) {
        try {
            cargarJson(event);
        } catch (IOException ex) {
            log.error("OCURRIO UN ERROR CARGANDO EL JSON", ex);
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_ERROR, "ERROR", "EL ARCHIVO CARGADO ESTA DAÑADO");
        }
    }

    private void cargarJson(FileUploadEvent event) throws IOException {
        if (!"application/json".equals(event.getFile().getContentType())) {
            throw new IllegalArgumentException("Solo se permiten archivos JSON");
        }
        String json = new String(event.getFile().getInputStream().readAllBytes());

        jsonObject = JsonParser.parseString(json).getAsJsonObject();

        jsonObject.remove("firmaElectronica");

        codigoGeneracion = jsonObject.get("identificacion").getAsJsonObject().get("codigoGeneracion").getAsString();
        fechEmi = jsonObject.get("identificacion").getAsJsonObject().get("fecEmi").getAsString();
        nitEmisor = jsonObject.get("emisor").getAsJsonObject().get("nit").getAsString();
        nombreEmisor = jsonObject.get("emisor").getAsJsonObject().get("nombre").getAsString();

        switch (jsonObject.get("identificacion").getAsJsonObject().get("tipoDte").getAsString()) {
            case "03":
                monto = new BigDecimal(jsonObject.get("resumen").getAsJsonObject().get("montoTotalOperacion").getAsString());
                break;
            case "09":
                monto = new BigDecimal(jsonObject.get("cuerpoDocumento").getAsJsonObject().get("liquidoApagar").getAsString());
                break;
            default:
                throw new AssertionError();
        }

        PrimeFaces.current().ajax().update("dvDetailBuy");
        PrimeFaces.current().executeScript("PF('dlgAddBuy').show();");

        log.info("FILE: " + event.getFile().getFileName());
    }

    public void guardarJson() {
        ResponseRestApi<ApiMhDteResponse> responseSendMh = buyService.save(gson.toJson(jsonObject), securityService.getToken());

        switch (responseSendMh.getCodeHttp()) {
            case 201:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_INFO)
                        .title("INFORMACIÓN")
                        .message("Compra registrada correctamente")
                        .build()
                        .showMessage();
                break;
            case 401:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title("ALERTA")
                        .message("EL JSON pertenece a otro contribuyente")
                        .build()
                        .showMessage();
                break;
            case 409:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title("ALERTA")
                        .message("Ya exite una compra con el código de generación: " + codigoGeneracion)
                        .build()
                        .showMessage();
                break;
            case 500:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_ERROR)
                        .title("ERROR")
                        .message("Ocurrio un error interno")
                        .build()
                        .showMessage();
                break;
        }
    }
}
