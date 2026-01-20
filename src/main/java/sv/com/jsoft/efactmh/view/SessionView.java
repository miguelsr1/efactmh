package sv.com.jsoft.efactmh.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.com.jsoft.efactmh.model.PlanMensual;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.services.SessionService;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ALERT;
import sv.com.jsoft.efactmh.util.JsfUtil;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
@Slf4j
public class SessionView implements Serializable {

    @Inject
    SessionService sessionService;

    private String idEstablecimiento;
    private String idPuntoVenta;

    @Getter
    @Setter
    private Boolean aceptaPagoPlazo = true;

    private String opcion = "/app/home.xhtml";
    //private boolean sinParametrosIniciales = false;

    @Getter
    private MenuModel model;
    @Getter
    private PlanMensual planMensual;

    @PostConstruct
    public void init() {
        loadCookies();
        loadMenu();
        loadPlanMensual();
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        if (opcion != null) {
            this.opcion = opcion;
        }
    }
    
    private void loadPlanMensual(){
        planMensual = sessionService.getPlanMensual().getBody();
    }

    private void loadCookies() {
        Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        if (requestCookieMap.containsKey("idEstable")) {
            idEstablecimiento = ((Cookie) requestCookieMap.get("idEstable")).getValue();
        }
        if (requestCookieMap.containsKey("idPuntoV")) {
            idPuntoVenta = ((Cookie) requestCookieMap.get("idPuntoV")).getValue();
        }
    }

    private void loadMenu() {
        model = new DefaultMenuModel();

        //First submenu
        DefaultSubMenu subMenuFav = DefaultSubMenu.builder()
                .label("Favoritos")
                .icon("pi pi-home")
                .expanded(true)
                .build();
        
        addSubMenu(subMenuFav, "Dashboard", "pi pi-chart-bar", "/app/dashboard.xhtml");

        model.getElements().add(DefaultMenuItem.builder()
                .value("Home")
                .icon("pi pi-home")
                .ajax(true)
                .command("#{sessionView.loadApp('/app/home.xhtml')}")
                .build());

        model.getElements().add(subMenuFav);

        DefaultSubMenu subMenuOpe = DefaultSubMenu.builder()
                .label("Operaciones")
                .icon("pi pi-fw pi-prime")
                .expanded(true)
                .build();

        addSubMenu(subMenuOpe, "Factura", "pi pi-inbox", "/app/process/invoce/invoce.xhtml");
        addSubMenu(subMenuOpe, "DTE's", "pi pi-list", "/app/lstDtes.xhtml");
        addSubMenu(subMenuOpe, "Ingreso de compras", "pi pi-shopping-bag", "/app/process/shopping/index.xhtml");
        
        model.getElements().add(subMenuOpe);
        
        DefaultSubMenu subMenuMan = DefaultSubMenu.builder()
                .label("Mantenimientos")
                .icon("pi pi-fw pi-prime")
                .expanded(true)
                .build();
        
        addSubMenu(subMenuMan, "Items", "pi pi-th-large", "/app/mantto/items.xhtml");
        addSubMenu(subMenuMan, "Mis Datos", "pi pi-user", "/app/mantto/emisor/emisor.xhtml");
        addSubMenu(subMenuMan, "Clientes", "pi pi-id-card", "/app/mantto/cliente.xhtml");
        
        model.getElements().add(subMenuMan);
    }

    private void addSubMenu(DefaultSubMenu subMenu, String opcion, String icon, String page) {
        subMenu.getElements().add(DefaultMenuItem.builder()
                .value(opcion)
                .icon(icon)
                .ajax(true)
                .command("#{sessionView.loadApp('" + page + "')}")
                .build());
    }

    public synchronized void loadApp(String app) {
        try {
            opcion = app;
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException ex) {
            log.error("ERROR EN LOAD MENU", ex);
        }
    }

    public void validateParamInitial() {
        if (idEstablecimiento == null || idPuntoVenta == null) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "ES REQUERIDO REALIZAR LA CONFIGURACIÓN INICIAL");
        }
    }

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(String idEstablecimiento) {
        JsfUtil.eliminarCookie("idPuntoV");
        idPuntoVenta = null;
        if (idEstablecimiento == null) {
            JsfUtil.eliminarCookie("idEstable");
        } else {
            JsfUtil.crearCookie("idEstable", idEstablecimiento);
            JsfUtil.eliminarCookie("idPuntoV");
            //sinParametrosIniciales = false;
        }
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getIdPuntoVenta() {
        return idPuntoVenta;
    }

    public void setIdPuntoVenta(String idPuntoVenta) {
        if (idPuntoVenta == null) {
            JsfUtil.eliminarCookie("idPuntoV");
        } else {
            JsfUtil.crearCookie("idPuntoV", idPuntoVenta);
            //sinParametrosIniciales = false;
        }
        this.idPuntoVenta = idPuntoVenta;
    }

    public String userName() {
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
