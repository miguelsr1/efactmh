package sv.com.jsoft.efactmh.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
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
