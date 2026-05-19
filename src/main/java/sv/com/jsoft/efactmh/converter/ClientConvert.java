package sv.com.jsoft.efactmh.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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
