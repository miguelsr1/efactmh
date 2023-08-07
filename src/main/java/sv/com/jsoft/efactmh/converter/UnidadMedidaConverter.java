package sv.com.jsoft.efactmh.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import sv.com.jsoft.efactmh.model.TipoUnidadMedida;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.view.ProductoView;

/**
 *
 * @author migue
 */
@FacesConverter(value = "unidadMedidaConverter")
public class UnidadMedidaConverter implements Converter {

    @Inject
    CatalogoService catSer;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value == null || value.length() == 0) {
            return new TipoUnidadMedida();
        }
        ProductoView controller = (ProductoView) fc.getApplication().getELResolver().
                getValue(fc.getELContext(), null, "productoView");
        return controller.getLstUnidadMedida().stream().filter(um -> um.getIdUnidadMedida().compareTo(Integer.valueOf(value)) == 0).findAny().orElse(null);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof TipoUnidadMedida) {
            TipoUnidadMedida o = (TipoUnidadMedida) object;
            return o.getIdUnidadMedida().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoUnidadMedida.class.getName());
        }
    }

}
