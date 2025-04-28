package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.services.SessionService;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionView implements Serializable{
    
    @Inject
    SessionService sessionService;
    
    private String idEstablecimiento;
    private String idPuntoVenta;
    
    @Getter
    @Setter
    private Boolean aceptaPagoPlazo = true;

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(String idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getIdPuntoVenta() {
        return idPuntoVenta;
    }

    public void setIdPuntoVenta(String idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
    }
    
    public String userName(){
        return sessionService.getUserName();
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().clear();
        context.getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
    
    public List<CatalogoDto> getLstPuntoVenta() {
        if (idEstablecimiento == null) {
            return new ArrayList<>();
        }
        return sessionService.getLstPuntoVenta(Long.valueOf(idEstablecimiento));
    }
}