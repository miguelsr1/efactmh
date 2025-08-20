package sv.com.jsoft.efactmh.util;

import java.util.HashMap;
import java.util.Map;
import software.xdev.chartjs.model.color.RGBAColor;

/**
 *
 * @author msanchez
 */
public class ColorUtil {

    private static Map<String, RGBAColor> colorBackground = new HashMap<>();
    private static Map<String, RGBAColor> colorBorder = new HashMap<>();

    private static Map<String, RGBAColor> obtenerColorBackground() {
        if (colorBackground.isEmpty()) {
            colorBackground = new HashMap<>();

            colorBackground.put("01", new RGBAColor(154, 208, 245, 0.9)); // azul - FE
            colorBackground.put("03", new RGBAColor(165, 223, 223, 0.9)); // verde CCFE
            //NOTAS: REMISION - CREDITO - DEBITO
            colorBackground.put("04", new RGBAColor(228, 229, 231, 0.9)); // Verde menta
            colorBackground.put("05", new RGBAColor(255, 230, 170, 0.9)); // Azul cielo
            colorBackground.put("06", new RGBAColor(255, 223, 186, 0.9)); // 
            
            colorBackground.put("99", new RGBAColor(255, 177, 193, 0.9)); // 
        }

        return colorBackground;
    }

    private static Map<String, RGBAColor> obtenerColorBorder() {
        if (colorBorder.isEmpty()) {
            colorBorder = new HashMap<>();

            colorBorder.put("01", new RGBAColor(154, 208, 245)); // Rosa pastel
            colorBorder.put("03", new RGBAColor(165, 223, 223)); // Durazno pastel
            //NOTAS: REMISION - CREDITO - DEBITO
            colorBorder.put("04", new RGBAColor(228, 229, 231)); // Amarillo pastel
            colorBorder.put("05", new RGBAColor(255, 230, 170)); // Verde menta
            colorBorder.put("06", new RGBAColor(255, 223, 186)); // Azul cielo
            
            colorBorder.put("99", new RGBAColor(255, 177, 193)); // Azul cielo
        }

        return colorBorder;
    }

    public static RGBAColor getColorBackgroundFromDte(String codigoDte) {
        return obtenerColorBackground().get(codigoDte);
    }
    
    public static RGBAColor getColorBorderFromDte(String codigoDte) {
        return obtenerColorBorder().get(codigoDte);
    }
    
}
