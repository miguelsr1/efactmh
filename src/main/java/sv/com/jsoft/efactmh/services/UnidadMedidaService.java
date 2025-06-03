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
                new CatalogoDto("1", "Metro"),
                new CatalogoDto("2", "Yarda"),
                new CatalogoDto("3", "Vara"),
                new CatalogoDto("4", "Pie"),
                new CatalogoDto("5", "Pulgada"),
                new CatalogoDto("6", "Milimetro"),
                new CatalogoDto("8", "Milla cuadrada"),
                new CatalogoDto("9", "Kilometro cuadrado"),
                new CatalogoDto("10", "Hectarea"),
                new CatalogoDto("11", "Manzana"),
                new CatalogoDto("12", "Acre"),
                new CatalogoDto("13", "Metro cuadrado"),
                new CatalogoDto("14", "Yarda cuadrada"),
                new CatalogoDto("15", "Vara cuadrada"),
                new CatalogoDto("16", "Pie cuadrado"),
                new CatalogoDto("17", "Pulgada cuadrada"),
                new CatalogoDto("18", "Metro cubico"),
                new CatalogoDto("19", "Yarda cubica"),
                new CatalogoDto("20", "Barril"),
                new CatalogoDto("21", "Pie cubico"),
                new CatalogoDto("22", "Galon"),
                new CatalogoDto("23", "Litro"),
                new CatalogoDto("24", "Botella"),
                new CatalogoDto("25", "Pulgada cubica"),
                new CatalogoDto("26", "Mililitro"),
                new CatalogoDto("27", "Onza fluida"),
                new CatalogoDto("29", "Tonelada metrica"),
                new CatalogoDto("30", "Tonelada"),
                new CatalogoDto("31", "Quintal metrico"),
                new CatalogoDto("32", "Quintal"),
                new CatalogoDto("33", "Arroba"),
                new CatalogoDto("34", "Kilogramo"),
                new CatalogoDto("35", "Libra troy"),
                new CatalogoDto("36", "Libra"),
                new CatalogoDto("37", "Onza troy"),
                new CatalogoDto("38", "Onza"),
                new CatalogoDto("39", "Gramo"),
                new CatalogoDto("40", "Miligramo"),
                new CatalogoDto("42", "Megawatt"),
                new CatalogoDto("43", "Kilowatt"),
                new CatalogoDto("44", "Watt"),
                new CatalogoDto("45", "Megavoltio-amperio"),
                new CatalogoDto("46", "Kilovoltio-amperio"),
                new CatalogoDto("47", "Voltio-amperio"),
                new CatalogoDto("49", "Gigawatt-hora"),
                new CatalogoDto("50", "Megawatt-hora"),
                new CatalogoDto("51", "Kilowatt-hora"),
                new CatalogoDto("52", "Watt-hora"),
                new CatalogoDto("53", "Kilovoltio"),
                new CatalogoDto("54", "Voltio"),
                new CatalogoDto("55", "Millar"),
                new CatalogoDto("56", "Medio millar"),
                new CatalogoDto("57", "Ciento"),
                new CatalogoDto("58", "Docena"),
                new CatalogoDto("59", "Unidad"),
                new CatalogoDto("99", "Otra")
        );
    }
}
