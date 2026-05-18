package sv.com.jsoft.efactmh.view;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.com.jsoft.efactmh.repository.ComprasRepository;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.MessageUtil;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Named
@ViewScoped
public class SaleView implements Serializable {

    @Getter
    @Setter
    private LocalDate fechaEmi;

    @Inject
    private ComprasRepository comprasRepository;
    @Inject
    private SessionService sessionService;

    public StreamedContent getFileConsumidorFinal() {
        if (fechaEmi == null) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title("ALERTA")
                    .message("Debe seleccionar una fecha")
                    .build()
                    .showMessage();
            return null;
        }

        String csvData = comprasRepository.getCsvConsumidorFinal(sessionService.getIdContribuyente().intValue(),
                fechaEmi.getYear(),
                fechaEmi.getMonthValue());

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
        String fileName = String.format("fe-%s.csv", fechaEmi.format(DateTimeFormatter.ofPattern("yyMM")));

        return DefaultStreamedContent.builder()
                .name(fileName)
                .contentType("text/csv")
                .stream(() -> stream)
                .build();
    }

    public StreamedContent getFileContribuyente() {
        if (fechaEmi == null) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title("ALERTA")
                    .message("Debe seleccionar una fecha")
                    .build()
                    .showMessage();
            return null;
        }

        String csvData = comprasRepository.getCsvContribuyente(sessionService.getIdContribuyente().intValue(),
                fechaEmi.getYear(),
                fechaEmi.getMonthValue());

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
        String fileName = String.format("ccf-%s.csv", fechaEmi.format(DateTimeFormatter.ofPattern("yyMM")));

        return DefaultStreamedContent.builder()
                .name(fileName)
                .contentType("text/csv")
                .stream(() -> stream)
                .build();
    }
}
