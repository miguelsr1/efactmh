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

            colorBackground.put("01", new RGBAColor(255, 179, 186, 0.2)); // Rosa pastel
            colorBackground.put("02", new RGBAColor(255, 223, 186, 0.2)); // Durazno pastel
            colorBackground.put("03", new RGBAColor(255, 255, 186, 0.2)); // Amarillo pastel
            colorBackground.put("04", new RGBAColor(186, 255, 201, 0.2)); // Verde menta
            colorBackground.put("05", new RGBAColor(186, 225, 255, 0.2)); // Azul cielo
        }

        return colorBackground;
    }

    private static Map<String, RGBAColor> obtenerColorBorder() {
        if (colorBorder.isEmpty()) {
            colorBorder = new HashMap<>();

            colorBorder.put("01", new RGBAColor(255, 179, 186)); // Rosa pastel
            colorBorder.put("02", new RGBAColor(255, 223, 186)); // Durazno pastel
            colorBorder.put("03", new RGBAColor(255, 255, 186)); // Amarillo pastel
            colorBorder.put("04", new RGBAColor(186, 255, 201)); // Verde menta
            colorBorder.put("05", new RGBAColor(186, 225, 255)); // Azul cielo
        }

        return colorBackground;
    }

    public static RGBAColor getColorBackgroundFromDte(String codigoDte) {
        return obtenerColorBackground().get(codigoDte);
    }
    
    public static RGBAColor getColorBorderFromDte(String codigoDte) {
        return obtenerColorBorder().get(codigoDte);
    }
    
}
