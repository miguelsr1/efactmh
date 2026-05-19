package sv.com.jsoft.efactmh.converter;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.services.CatalogoService;

/**
 *
 * @author migue
 */
@Named
@FacesConverter(value = "productoConverter", managed = true)
@Slf4j
public class ProductoConverter implements Converter<Producto> {

    @Inject
    private CatalogoService catService;

    @Override
    public Producto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                log.info(value);
                return null; 
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid country."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Producto value) {
        log.info(value.toString());
        if (value != null) {
            return value.getCodigoItem();
        } else {
            return null;
        }
    }
}
