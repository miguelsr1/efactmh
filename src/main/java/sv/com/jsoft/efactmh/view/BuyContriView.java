package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.BuyDtoResponse;
import sv.com.jsoft.efactmh.services.BuyService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Slf4j
public class BuyContriView implements Serializable {

    @Getter
    @Setter
    private int idTipoDocumento;
    @Getter
    @Setter
    private String numeroDocumento;

    @Getter
    @Setter
    private LocalDate buyDate;
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
    @Setter
    private Integer currentYear;
    @Getter
    @Setter
    private Integer selectedMonth;
    @Getter
    @Setter
    private int idCostClassification;

    @Getter
    private List<BuyDtoResponse> lstBuys;
    @Getter
    private List<String> failedFiles;

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    @Inject
    SessionService securityService;
    @Inject
    @Getter
    BuyService buyService;

    @PostConstruct
    public void init() {
        idTipoDocumento = 13;
        lstBuys = new ArrayList<>();
        failedFiles = new ArrayList<>();
        idCostClassification = 1;
        currentYear = LocalDate.now().getYear();
    }

    public void handleFileUpload(FileUploadEvent event) {
        failedFiles.clear();
        UploadedFile uploadedFile = event.getFile();
        if (uploadedFile != null) {
            try {
                boolean success = procesarYGuardarJson(uploadedFile);
                if (success) {
                    MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_INFO)
                        .title("INFORMACIÓN")
                        .message("Archivo " + uploadedFile.getFileName() + " procesado correctamente.")
                        .build()
                        .showMessage();
                } else {
                    String fallo = failedFiles.isEmpty() ? "Error desconocido" : failedFiles.get(0);
                    MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title("ALERTA")
                        .message("No se pudo guardar " + uploadedFile.getFileName() + ". " + fallo)
                        .build()
                        .showMessage();
                }
            } catch (Exception ex) {
                log.error("OCURRIO UN ERROR CARGANDO EL JSON: " + uploadedFile.getFileName(), ex);
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_ERROR)
                        .title("ERROR")
                        .message("Error interno procesando " + uploadedFile.getFileName())
                        .build()
                        .showMessage();
            }
        }
        loadBuys();
    }

    private boolean procesarYGuardarJson(UploadedFile uploadedFile) throws IOException {
        if (!"application/json".equals(uploadedFile.getContentType())) {
            failedFiles.add(uploadedFile.getFileName() + " (No es un JSON válido)");
            return false;
        }
        String json = new String(uploadedFile.getInputStream().readAllBytes());

        JsonObject currentJsonObject = JsonParser.parseString(json).getAsJsonObject();

        String currentFechEmi = currentJsonObject.get("identificacion").getAsJsonObject().get("fecEmi").getAsString();
        LocalDate currentFechaEmi = LocalDate.parse(currentFechEmi);

        if (currentFechaEmi.getMonthValue() != selectedMonth || currentFechaEmi.getYear() != currentYear) {
            failedFiles.add(uploadedFile.getFileName() + " (Mes/Año incorrecto)");
            return false;
        }

        currentJsonObject.remove("firmaElectronica");

        ResponseRestApi<ApiMhDteResponse> responseSendMh = buyService.save(gson.toJson(currentJsonObject), buyDate, idTipoDocumento, numeroDocumento);

        if (responseSendMh.getCodeHttp() == 201) {
            return true;
        } else {
            failedFiles.add(uploadedFile.getFileName() + " (" + (responseSendMh.getErrorMessageDto() != null ? responseSendMh.getErrorMessageDto().getErrorMessage() : "Error API") + ")");
            return false;
        }
    }

    public void onMonthSelect() {
        if (selectedMonth != null && currentYear != null) {
            fecha = LocalDate.of(currentYear, selectedMonth, 1);
            buyDate = fecha;
            loadBuys();
        }
    }

    private void loadBuys() {
        if (fecha != null && numeroDocumento != null && !numeroDocumento.trim().isEmpty()) {
            lstBuys = buyService.getListContri(fecha, idTipoDocumento, numeroDocumento);
        } else {
            lstBuys.clear();
        }
    }

    public void prepareViewDetail(BuyDtoResponse buy) {
        this.codigoGeneracion = buy.getCodigoGeneracion();
        this.fechEmi = buy.getFecha() != null ? buy.getFecha() : "";
        this.nitEmisor = buy.getNit();
        this.nombreEmisor = buy.getNombre();
        this.monto = buy.getMonto();
        PrimeFaces.current().executeScript("PF('dlgViewBuy').show();");
    }

    public int getMaxNumDoc() {
        if (idTipoDocumento == 13) {
            return 9;
        } else {
            return 14;
        }
    }
}
