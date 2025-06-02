package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Departamento;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class UnidadMedidaService implements Serializable {

    @Getter
    private List<Departamento> lstUnidadesMedidas;

    @PostConstruct
    public void init() {
        lstUnidadesMedidas = Arrays.asList(
                new Departamento("1", "Metro"),
                new Departamento("2", "Yarda"),
                new Departamento("3", "Vara"),
                new Departamento("4", "Pie"),
                new Departamento("5", "Pulgada"),
                new Departamento("6", "Milimetro"),
                new Departamento("8", "Milla cuadrada"),
                new Departamento("9", "Kilometro cuadrado"),
                new Departamento("10", "Hectarea"),
                new Departamento("11", "Manzana"),
                new Departamento("12", "Acre"),
                new Departamento("13", "Metro cuadrado"),
                new Departamento("14", "Yarda cuadrada"),
                new Departamento("15", "Vara cuadrada"),
                new Departamento("16", "Pie cuadrado"),
                new Departamento("17", "Pulgada cuadrada"),
                new Departamento("18", "Metro cubico"),
                new Departamento("19", "Yarda cubica"),
                new Departamento("20", "Barril"),
                new Departamento("21", "Pie cubico"),
                new Departamento("22", "Galon"),
                new Departamento("23", "Litro"),
                new Departamento("24", "Botella"),
                new Departamento("25", "Pulgada cubica"),
                new Departamento("26", "Mililitro"),
                new Departamento("27", "Onza fluida"),
                new Departamento("29", "Tonelada metrica"),
                new Departamento("30", "Tonelada"),
                new Departamento("31", "Quintal metrico"),
                new Departamento("32", "Quintal"),
                new Departamento("33", "Arroba"),
                new Departamento("34", "Kilogramo"),
                new Departamento("35", "Libra troy"),
                new Departamento("36", "Libra"),
                new Departamento("37", "Onza troy"),
                new Departamento("38", "Onza"),
                new Departamento("39", "Gramo"),
                new Departamento("40", "Miligramo"),
                new Departamento("42", "Megawatt"),
                new Departamento("43", "Kilowatt"),
                new Departamento("44", "Watt"),
                new Departamento("45", "Megavoltio-amperio"),
                new Departamento("46", "Kilovoltio-amperio"),
                new Departamento("47", "Voltio-amperio"),
                new Departamento("49", "Gigawatt-hora"),
                new Departamento("50", "Megawatt-hora"),
                new Departamento("51", "Kilowatt-hora"),
                new Departamento("52", "Watt-hora"),
                new Departamento("53", "Kilovoltio"),
                new Departamento("54", "Voltio"),
                new Departamento("55", "Millar"),
                new Departamento("56", "Medio millar"),
                new Departamento("57", "Ciento"),
                new Departamento("58", "Docena"),
                new Departamento("59", "Unidad"),
                new Departamento("99", "Otra")
        );
    }
}
