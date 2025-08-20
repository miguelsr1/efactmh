package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import sv.com.jsoft.efactmh.model.dto.Invoice7DaysDto;
import sv.com.jsoft.efactmh.model.dto.InvoicedAmountsDto;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.services.DashboardService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.ColorUtil;
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
    CatalogoService catalogoService;
    @Inject
    DashboardService dashboardService;
    @Inject
    SessionService securityService;
    @Getter
    private List<InvoicedAmountsDto> lstInvoiced = new ArrayList<>();
    @Getter
    private String model;
    @Getter
    private String barModel;
    
    private List<DashboardDto> lst;
    private Collection<String> dias = null;

    @PostConstruct
    public void init() {
        dias = obtenerNombresUltimos7Dias();
    }

    public void loadData() {

        makeChartInvoce();

        loadDataInvoice7Days();
        
        loadInvoicedAmounts();
    }
    
    private void loadInvoicedAmounts(){
        ResponseRestApi<List<InvoicedAmountsDto>> response = dashboardService.getInvoicedAmounts(securityService.getToken());
        
        if (response.getCodeHttp() == 200) {
            lstInvoiced = response.getBody();
        }
    }

    private void loadDataInvoice7Days() {
        List<Invoice7DaysDto> lstData = new ArrayList<>();
        ResponseRestApi<List<Invoice7DaysDto>> response = dashboardService.getInvoice7Days(securityService.getToken());

        if (response.getCodeHttp() == 200) {
            lstData = response.getBody();
        } else if (response.getCodeHttp() == 404) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_INFO)
                    .title(MSG_INFO)
                    .message("NO HAY DATOS PARA MOSTRAR")
                    .build()
                    .showMessage();
        }

        //obtener los tipos de codigosDte
        Set<String> codigosDte = lstData.stream()
                .map(Invoice7DaysDto::getCodigoDte)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        BarData barData = new BarData();
        

        for (String dte : codigosDte) {
            barData.addDataset(makeDateFromDte(dte, lstData));
        }
        
        barData.setLabels(dias);

        BarChart barChart = new BarChart()
                .setData(barData)
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
                                        .setText("Tipos de DTE últimos 7 días")))
                );

        barModel = barChart.toJson();
    }

    private BarDataset makeDateFromDte(String codigoDte, List<Invoice7DaysDto> lstData) {
        Collection<Number> lstSerie = new ArrayList<>();

        // Rango fijo de fechas: del 13 al 19 de agosto de 2025
        LocalDate fechaInicio = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate fechaActual = fechaInicio.minusDays(i);

            Optional<Invoice7DaysDto> registroOpt = lstData.stream()
                    .filter(r -> codigoDte.equals(r.getCodigoDte())
                    && LocalDate.parse(r.getFecha()).equals(fechaActual))
                    .findFirst();

            BigDecimal monto = registroOpt.map(Invoice7DaysDto::getMonto).orElse(BigDecimal.ZERO);
            lstSerie.add(monto);
        }
        
        return new BarDataset()
                .setData(lstSerie)
                .setLabel(catalogoService.getDtes().get(codigoDte))
                .setBackgroundColor(ColorUtil.getColorBackgroundFromDte(codigoDte))
                .setBorderColor(ColorUtil.getColorBorderFromDte(codigoDte))
                .setBorderWidth(1)
                .setCategoryPercentage(0.2);
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

        //Collection<String> dias = obtenerNombresUltimos7Dias();
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

    public void makeChartInvoce() {
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

        Collection<Number> numeros = flujoFacturadoUltimaSemana();

        Collection<String> dias = obtenerNombresUltimos7Dias();

        model = new LineChart()
                .setData(new LineData()
                        .addDataset(new LineDataset()
                                .setData(numeros)
                                .setLabel("Facturado por día")
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
                                        .setText("Facturado de últimos 7 días")))
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
                .map(Map.Entry::getValue) // Extrae solo el valor (el monto BigDecimal).
                .map(monto -> (Number) monto) // Convierte cada BigDecimal a Number.
                .collect(Collectors.toList());
    }

    public Collection<String> obtenerNombresUltimos7Dias() {
        Locale locale = new Locale("es", "ES"); // Cambia a "en" para inglés
        LocalDate hoy = LocalDate.now();

        return IntStream.rangeClosed(0, 6)
                .mapToObj(i -> hoy.minusDays(6 - i)) // orden cronológico: de más antiguo a hoy
                .map(fecha -> fecha.getDayOfWeek().getDisplayName(TextStyle.SHORT.SHORT, locale).concat("-" + fecha.getDayOfMonth())) // lunes, martes...
                .collect(Collectors.toList());
    }

}
