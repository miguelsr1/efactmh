package sv.com.jsoft.efactmh.converter;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.services.ClientService;

/**
 *
 * @author msanchez
 */
@Named
@ApplicationScoped
@FacesConverter(value = "clientConverter", managed = true)
public class ClientConvert implements Converter<ClienteResponse> {

    @Inject
    ClientService clientService;

    @Override
    public ClienteResponse getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return clientService.get(Long.valueOf(value));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid country."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ClienteResponse value) {
        if (value != null && value.getIdCliente() != null) {
            return String.valueOf(value.getIdCliente());
        } else {
            return null;
        }
    }

}
