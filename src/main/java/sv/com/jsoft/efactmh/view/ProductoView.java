package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.ErrorMessageDto;
import sv.com.jsoft.efactmh.model.enums.TipoMensaje;
import sv.com.jsoft.efactmh.services.ProductoService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.services.TipoItemService;
import sv.com.jsoft.efactmh.services.UnidadMedidaService;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ERROR;
import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class ProductoView implements Serializable {

    @Getter
    @Setter
    private boolean disabled = true;
    private boolean edit = false;

    private Producto producto;
    @Getter
    private List<Producto> lstProducto;

    @Inject
    SessionService sessionService;
    @Inject
    ProductoService productoService;
    @Inject
    UnidadMedidaService unidadMedidaService;
    @Inject
    TipoItemService tipoItemService;

    @PostConstruct
    public void init() {
        producto = new Producto();
        lstProducto = productoService.findAll(sessionService.getToken());
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        if (producto != null) {
            this.producto = producto;
        }
    }

    public void guardar() {
        disabled = true;
        ResponseRestApi response;
        if (edit) {
            RestUtil
                    .builder()
                    .clazz(Producto.class)
                    .jwtDto(sessionService.getToken())
                    .body(producto)
                    .endpoint("/api/item/" + producto.getIdProducto())
                    .build()
                    .callPutAuth();
        } else {
            response = RestUtil
                    .builder()
                    .clazz(Producto.class)
                    .jwtDto(sessionService.getToken())
                    .body(producto)
                    .endpoint("/api/item/")
                    .build()
                    .callPostAuth();

            if (response.getCodeHttp() == 201) {
                JsfUtil.mensajeFromEnum(TipoMensaje.INSERT);
            } else {
                ErrorMessageDto errorMessage = new Gson().fromJson(response.getBody().toString(), ErrorMessageDto.class);
                MessageUtil.builder()
                        .message(errorMessage.getErrorMessage())
                        .severity(FacesMessage.SEVERITY_ERROR)
                        .title(MSG_ERROR)
                        .build()
                        .showMessage();
            }
        }

        cancelar();
    }

    public void nuevo() {
        producto = new Producto();
        producto.setCodigoItem("1");
        disabled = false;
        edit = false;
    }

    public void cancelar() {
        producto = new Producto();
        disabled = true;
        edit = false;

        lstProducto = productoService.findAll(sessionService.getToken());
    }

    public void onRowSelect(SelectEvent<Producto> event) {
        producto = event.getObject();
        disabled = false;
        edit = true;
    }

    public String descripcionUnidadMedida(String codigoUnidad) {
        return unidadMedidaService
                .getLstUnidadesMedidas()
                .stream()
                .filter(uni -> uni.getId().equals(codigoUnidad))
                .findFirst().get().getNombre();
    }

    public String descripcionTipoItem(String codigoItem) {
        return tipoItemService
                .getLstTipoItems()
                .stream()
                .filter(uni -> uni.getId().equals(codigoItem))
                .findFirst().get().getNombre();
    }
}
