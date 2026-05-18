package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.BuyDtoResponse;
import sv.com.jsoft.efactmh.model.dto.CostClassificationDto;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.repository.ComprasRepository;
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
    private int idCostClassification;
    @Getter
    @Setter
    private LocalDate buyDate;
    @Getter
    @Setter
    private UploadedFile file;
    
    @Getter
    @Setter
    private UploadedFiles files;
    
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
    @Setter
    private Integer currentYear;
    
    @Getter
    @Setter
    private Integer selectedMonth;

    @Getter
    private List<BuyDtoResponse> lstBuys;

    @Getter
    private List<String> failedFiles;

    @Inject
    SessionService securityService;
    
    @Inject
    @Getter
    BuyService buyService;
    
    @Inject
    ComprasRepository comprasRepository;

    @PostConstruct
    public void init() {
        lstBuys = new ArrayList<>();
        failedFiles = new ArrayList<>();
        idCostClassification = 1;
        currentYear = LocalDate.now().getYear();
    }

    public String getIconCost() {
        return getLstCost()
                .stream()
                .filter(cost -> cost.getId() == idCostClassification)
                .findFirst().get().getIcon();
    }

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private JsonObject jsonObject;

    public void handleFilesUpload(FilesUploadEvent event) {
        failedFiles.clear();
        int successCount = 0;
        
        if (event.getFiles() != null && event.getFiles().getFiles() != null) {
            for (UploadedFile uploadedFile : event.getFiles().getFiles()) {
                try {
                    boolean success = procesarYGuardarJson(uploadedFile);
                    if (success) {
                        successCount++;
                    }
                } catch (Exception ex) {
                    log.error("OCURRIO UN ERROR CARGANDO EL JSON: " + uploadedFile.getFileName(), ex);
                    failedFiles.add(uploadedFile.getFileName() + " (Error de lectura)");
                }
            }
        }
        
        if (failedFiles.isEmpty()) {
             MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_INFO)
                        .title("INFORMACIÓN")
                        .message("Se han procesado " + successCount + " archivos correctamente.")
                        .build()
                        .showMessage();
        } else {
            String fallidos = String.join(", ", failedFiles);
            MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title("ALERTA")
                        .message("Se guardaron " + successCount + " archivos. Fallaron: " + fallidos)
                        .build()
                        .showMessage();
        }
        
        loadBuys();
    }

    public void handleFileUpload(FileUploadEvent event) {
        // En PrimeFaces, si usas auto="false" o si envias archivos uno por uno, a veces se dispara
        // este evento individual en lugar del masivo (FilesUploadEvent).
        // Lo redirigimos a nuestra logica unitaria de guardado.
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

    public List<CostClassificationDto> getLstCost() {
        return buyService.getLstCost();
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

        if (currentFechaEmi.getMonth() != fecha.getMonth()
                || currentFechaEmi.getYear() != fecha.getYear()) {
            failedFiles.add(uploadedFile.getFileName() + " (Mes/Año incorrecto)");
            return false;
        }

        currentJsonObject.remove("firmaElectronica");
        
        ResponseRestApi<ApiMhDteResponse> responseSendMh = buyService.save(gson.toJson(currentJsonObject), buyDate, securityService.getToken());

        if (responseSendMh.getCodeHttp() == 201) {
            return true;
        } else {
             failedFiles.add(uploadedFile.getFileName() + " (" + (responseSendMh.getErrorMessageDto() != null ? responseSendMh.getErrorMessageDto().getErrorMessage() : "Error API") + ")");
             return false;
        }
    }
    
    public void prepareViewDetail(BuyDtoResponse buy) {
         // Logica para obtener y mostrar detalle. Por ahora asignamos campos basicos si estan disponibles o requeriremos una llamada a base de datos si el Dto no tiene todo.
         this.codigoGeneracion = buy.getCodigoGeneracion();
         this.fechEmi = buy.getFecha() != null ? buy.getFecha().toString() : "";
         this.nitEmisor = buy.getNit();
         this.nombreEmisor = buy.getNombre();
         this.monto = buy.getMonto();
         // En un escenario real, tendrias que obtener el JSON original y extraer los valores exactos, o tener un endpoint para obtener el detalle de una compra.
         // Aqui reusamos las propiedades actuales del bean para que dlgAddBuy (renombrado a algo como dlgViewBuy) pueda mostrarlos.
         PrimeFaces.current().executeScript("PF('dlgViewBuy').show();");
    }

    public void onDateSelect(SelectEvent<LocalDate> event) {
        lstBuys.clear();
        fecha = event.getObject();
        loadBuys();
    }

    public void onMonthSelect() {
        if (selectedMonth != null && currentYear != null) {
            lstBuys.clear();
            fecha = LocalDate.of(currentYear, selectedMonth, 1);
            buyDate = fecha;
            loadBuys();
        }
    }

    private void loadBuys() {
        lstBuys = buyService.getList(fecha, securityService.getToken());
    }

    public void guardarJson() {
        // Este metodo ya no se usaria con la nueva logica de carga masiva directa, pero se deja por compatibilidad si aun es llamado
    }

    public StreamedContent getFileCsv() {
        if (fecha == null) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title("ALERTA")
                    .message("Debe seleccionar una fecha")
                    .build()
                    .showMessage();
            return null;
        }

        Long idContribuyente = securityService.getIdContribuyente();

        String csvData = comprasRepository.getCsvCompras(idContribuyente, fecha.getYear(), fecha.getMonthValue());

        if (csvData == null || csvData.isEmpty()) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title("ALERTA")
                    .message("No se encontraron datos para generar el reporte.")
                    .build()
                    .showMessage();
            return null;
        }

        InputStream stream = new ByteArrayInputStream(csvData.getBytes());
        String fileName = String.format("compra-%s.csv", fecha.format(DateTimeFormatter.ofPattern("yyMM")));

        return DefaultStreamedContent.builder()
                .name(fileName)
                .contentType("text/csv")
                .stream(() -> stream)
                .build();
    }
}