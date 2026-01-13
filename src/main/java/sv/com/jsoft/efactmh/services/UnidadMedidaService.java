package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class UnidadMedidaService implements Serializable {

    @Getter
    private List<CatalogoDto> lstUnidadesMedidas;

    @PostConstruct
    public void init() {
        lstUnidadesMedidas = Arrays.asList(
                new CatalogoDto("1", "METRO"),
                new CatalogoDto("2", "YARDA"),
                new CatalogoDto("3", "VARA"),
                new CatalogoDto("4", "PIE"),
                new CatalogoDto("5", "PULGADA"),
                new CatalogoDto("6", "MILIMETRO"),
                new CatalogoDto("8", "MILLA CUADRADA"),
                new CatalogoDto("9", "KILOMETRO CUADRADO"),
                new CatalogoDto("10", "HECTAREA"),
                new CatalogoDto("11", "MANZANA"),
                new CatalogoDto("12", "ACRE"),
                new CatalogoDto("13", "METRO CUADRADO"),
                new CatalogoDto("14", "YARDA CUADRADA"),
                new CatalogoDto("15", "VARA CUADRADA"),
                new CatalogoDto("16", "PIE CUADRADO"),
                new CatalogoDto("17", "PULGADA CUADRADA"),
                new CatalogoDto("18", "METRO CUBICO"),
                new CatalogoDto("19", "YARDA CUBICA"),
                new CatalogoDto("20", "BARRIL"),
                new CatalogoDto("21", "PIE CUBICO"),
                new CatalogoDto("22", "GALON"),
                new CatalogoDto("23", "LITRO"),
                new CatalogoDto("24", "BOTELLA"),
                new CatalogoDto("25", "PULGADA CUBICA"),
                new CatalogoDto("26", "MILILITRO"),
                new CatalogoDto("27", "ONZA FLUIDA"),
                new CatalogoDto("29", "TONELADA METRICA"),
                new CatalogoDto("30", "TONELADA"),
                new CatalogoDto("31", "QUINTAL METRICO"),
                new CatalogoDto("32", "QUINTAL"),
                new CatalogoDto("33", "ARROBA"),
                new CatalogoDto("34", "KILOGRAMO"),
                new CatalogoDto("35", "LIBRA TROY"),
                new CatalogoDto("36", "LIBRA"),
                new CatalogoDto("37", "ONZA TROY"),
                new CatalogoDto("38", "ONZA"),
                new CatalogoDto("39", "GRAMO"),
                new CatalogoDto("40", "MILIGRAMO"),
                new CatalogoDto("42", "MEGAWATT"),
                new CatalogoDto("43", "KILOWATT"),
                new CatalogoDto("44", "WATT"),
                new CatalogoDto("45", "MEGAVOLTIO-AMPERIO"),
                new CatalogoDto("46", "KILOVOLTIO-AMPERIO"),
                new CatalogoDto("47", "VOLTIO-AMPERIO"),
                new CatalogoDto("49", "GIGAWATT-HORA"),
                new CatalogoDto("50", "MEGAWATT-HORA"),
                new CatalogoDto("51", "KILOWATT-HORA"),
                new CatalogoDto("52", "WATT-HORA"),
                new CatalogoDto("53", "KILOVOLTIO"),
                new CatalogoDto("54", "VOLTIO"),
                new CatalogoDto("55", "MILLAR"),
                new CatalogoDto("56", "MEDIO MILLAR"),
                new CatalogoDto("57", "CIENTO"),
                new CatalogoDto("58", "DOCENA"),
                new CatalogoDto("59", "UNIDAD"),
                new CatalogoDto("99", "OTRA")
        );
    }
}
