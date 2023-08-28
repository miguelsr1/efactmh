package sv.com.jsoft.efactmh.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.services.CatalogoService;

/**
 *
 * @author migue
 */
@Named
@FacesConverter(value = "productoConverter", managed = true)
public class ProductoConverter implements Converter<Producto> {

    @Inject
    private CatalogoService catService;

    @Override
    public Producto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println(value);
                return catService.getProductoByCodigo(value);
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid country."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Producto value) {
        System.out.println(value);
        if (value != null) {
            return value.getCodigo();
        } else {
            return null;
        }
    }
}
