package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.charts.LineChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.data.LineData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.dataset.LineDataset;
import software.xdev.chartjs.model.enums.IndexAxis;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LineOptions;
import software.xdev.chartjs.model.options.Plugins;
import software.xdev.chartjs.model.options.Title;
import software.xdev.chartjs.model.options.elements.Fill;
import software.xdev.chartjs.model.options.scale.Scales;
import software.xdev.chartjs.model.options.scale.cartesian.CartesianScaleOptions;
import software.xdev.chartjs.model.options.scale.cartesian.CartesianTickOptions;
import sv.com.jsoft.efactmh.model.dto.DashboardDto;
import sv.com.jsoft.efactmh.services.DashboardService;
import sv.com.jsoft.efactmh.services.SessionService;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_INFO;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;

/**
 *
 * @author msanchez
 */
@ViewScoped
@Named
public class DashboardView implements Serializable {

    @Inject
    DashboardService dashboardService;
    @Inject
    SessionService securityService;

    private List<DashboardDto> lst;
    @Getter
    private String model;

    @PostConstruct
    public void init() {

    }

    public void loadData() {
        ResponseRestApi<List<DashboardDto>> response = dashboardService.findAllData(securityService.getToken());
        if (response.getCodeHttp() == 200) {
            lst = response.getBody();
        } else if (response.getCodeHttp() == 404) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_INFO)
                    .title(MSG_INFO)
                    .message("NO HAY DATOS PARA MOSTRAR")
                    .build()
                    .showMessage();
        }

        createLineModel();
    }

    public Integer getTotalInvoces() {
        return lst.size() - 1;

    }

    public Long getTotalCustomers() {
        return lst.stream()
                .map(DashboardDto::getNumDocumento)
                .distinct()
                .count();
    }

    public BigDecimal getTotalBilled() {
        return lst.stream()
                .map(DashboardDto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public Long getTotalCancellations() {
        return lst.stream()
                .filter(dto -> dto.getEstado() == 3)
                .count();

    }

    public void createBarModel() {
        Collection<Number> numeros = flujoFacturadoUltimaSemana();

        Collection<String> dias = obtenerNombresUltimos7Dias();

        model = new BarChart()
                .setData(new BarData()
                        .addDataset(new BarDataset()
                                .setData(numeros)
                                .setLabel("Total Facturado")
                                .setBackgroundColor(new RGBAColor(255, 99, 132, 0.2))
                                .setBorderColor(new RGBAColor(255, 99, 132))
                                .setBorderWidth(1))
                        .setLabels(dias))
                .setOptions(new BarOptions()
                        .setResponsive(true)
                        .setMaintainAspectRatio(false)
                        .setIndexAxis(IndexAxis.X)
                        .setScales(new Scales().addScale(Scales.ScaleAxis.Y, new CartesianScaleOptions()
                                .setStacked(false)
                                .setTicks(new CartesianTickOptions()
                                        .setAutoSkip(true)
                                        .setMirror(true)))
                        )
                        .setPlugins(new Plugins()
                                .setTitle(new Title()
                                        .setDisplay(true)
                                        .setText("Facturado de últimos 7 días")))
                ).toJson();
    }
    
    public void createLineModel() {
        Collection<Number> numeros = flujoFacturadoUltimaSemana();
        
        Collection<String> dias = obtenerNombresUltimos7Dias();
        
        model = new LineChart()
                .setData(new LineData()
                .addDataset(new LineDataset()
                        .setData(numeros)
                        .setLabel("My First Dataset")
                        .setBorderColor(new RGBAColor(34, 150, 243))
                        .setLineTension(0.5f)
                        .setBackgroundColor("GRADIENT")
                        .setFill(new Fill<Boolean>(true)))
                .setLabels(dias))
                .setOptions(new LineOptions()
                        .setResponsive(true)
                        .setAnimation(true)
                        .setMaintainAspectRatio(false)
                        .setPlugins(new Plugins()
                                .setTitle(new Title()
                                        .setDisplay(true)
                                        .setText("Line Chart Subtitle")))
                ).toJson();
    }


    public Collection<Number> flujoFacturadoUltimaSemana() {
        LocalDate hoy = LocalDate.now();
        LocalDate hace7dias = hoy.minusDays(6);

        Map<LocalDate, BigDecimal> flujoPorDia = lst.stream()
                .filter(dto -> dto.getFechaCreacion() != null)
                .filter(dto -> {
                    LocalDate fecha = dto.getFechaCreacion().toLocalDate();
                    return !fecha.isBefore(hace7dias) && !fecha.isAfter(hoy);
                })
                .collect(Collectors.groupingBy(
                        dto -> dto.getFechaCreacion().toLocalDate(),
                        Collectors.mapping(
                                DashboardDto::getMonto,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        
        return flujoPorDia.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // Ordena las entradas por la fecha (LocalDate).
            .map(Map.Entry::getValue)           // Extrae solo el valor (el monto BigDecimal).
            .map(monto -> (Number) monto)       // Convierte cada BigDecimal a Number.
            .collect(Collectors.toList());
    }

    public Collection<String> obtenerNombresUltimos7Dias() {
        Locale locale = new Locale("es", "ES"); // Cambia a "en" para inglés
        LocalDate hoy = LocalDate.now();

        return IntStream.rangeClosed(0, 6)
                .mapToObj(i -> hoy.minusDays(6 - i)) // orden cronológico: de más antiguo a hoy
                .map(fecha -> fecha.getDayOfWeek().getDisplayName(TextStyle.FULL.FULL, locale)) // lunes, martes...
                .collect(Collectors.toList());
    }

}
