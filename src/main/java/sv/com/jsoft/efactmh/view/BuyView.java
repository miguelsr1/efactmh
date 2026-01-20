package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.BuyDtoResponse;
import sv.com.jsoft.efactmh.model.dto.ErrorMessageDto;
import sv.com.jsoft.efactmh.services.BuyService;
import sv.com.jsoft.efactmh.services.SessionService;
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

    private String codigoDte;
    private LocalDate fechaEmi;

    @Getter
    @Setter
    private LocalDate buyDate;
    @Getter
    @Setter
    private UploadedFile file;
    @Getter
    private String nombreDte;
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
    @Getter
    private LocalDate fecha;

    @Getter
    private List<BuyDtoResponse> lstBuys;

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

        fechEmi = jsonObject.get("identificacion").getAsJsonObject().get("fecEmi").getAsString();
        fechaEmi = LocalDate.parse(fechEmi);

        if (fechaEmi.getMonth() != fecha.getMonth()
                || fechaEmi.getYear() != fecha.getYear()) {
             MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title("ALERTA")
                        .message("EL DTE INGRESADO NO ES DEL MES Y AÑO SELECCIONADO. FECHA DOCUMENTO: " + fechEmi)
                        .build()
                        .showMessage();
             return;
        }

        jsonObject.remove("firmaElectronica");

        codigoDte = jsonObject.get("identificacion").getAsJsonObject().get("tipoDte").getAsString();
        codigoGeneracion = jsonObject.get("identificacion").getAsJsonObject().get("codigoGeneracion").getAsString();
        nitEmisor = jsonObject.get("emisor").getAsJsonObject().get("nit").getAsString();
        nombreEmisor = jsonObject.get("emisor").getAsJsonObject().get("nombre").getAsString();

        switch (jsonObject.get("identificacion").getAsJsonObject().get("tipoDte").getAsString()) {
            case "01":
                nombreDte = "FACTURA ELECTRONICA";
                break;
            case "03":
                nombreDte = "COMPROBANTE CREDITO FISCAL";
                monto = new BigDecimal(jsonObject.get("resumen").getAsJsonObject().get("montoTotalOperacion").getAsString());
                break;
            case "09":
                nombreDte = "DOCUMENTO CONTABLE DE LIQUIDACION";
                monto = new BigDecimal(jsonObject.get("cuerpoDocumento").getAsJsonObject().get("liquidoApagar").getAsString());
                break;
            default:
                throw new AssertionError();
        }

        PrimeFaces.current().ajax().update("dvDetailBuy");
        PrimeFaces.current().executeScript("PF('dlgAddBuy').show();");

        log.info("FILE: " + event.getFile().getFileName());

        loadBuys();
    }

    public void onDateSelect(SelectEvent<LocalDate> event) {
        fecha = event.getObject();
        loadBuys();
    }

    private void loadBuys() {
        lstBuys = buyService.getList(fecha, securityService.getToken());
    }

    public void guardarJson() {
        ResponseRestApi<ApiMhDteResponse> responseSendMh = buyService.save(gson.toJson(jsonObject), buyDate, securityService.getToken());

        PrimeFaces.current().executeScript("PF('dlgAddBuy').hide();");

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
            case 409:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title("ALERTA")
                        .message(responseSendMh.getErrorMessageDto().getErrorMessage())
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
