package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.ItemDto;
import sv.com.jsoft.efactmh.model.dto.DteToInvalidate;
import sv.com.jsoft.efactmh.model.dto.DtesResponse;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
import sv.com.jsoft.efactmh.services.InvalidateService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class DtesView implements Serializable {

    @Getter
    @Setter
    private Long idFactura;
    @Getter
    @Setter
    private String codigoDte;
    @Getter
    @Setter
    private String codigoGeneracion;
    @Getter
    @Setter
    private ItemDto itemDto;
    private List<DtesResponse> lstDtes;

    @Inject
    SessionService sessionService;
    @Inject
    InvalidateService invalidateService;

    @PostConstruct
    public void init() {
        lstDtes = new ArrayList<>();
    }

    public List<DtesResponse> getLstDtes() {
        return lstDtes;
    }

    public void findDtes() {
        RestUtil rest = RestUtil.builder()
                .clazz(DtesResponse.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/secured/dte/all")
                .build();
        ResponseRestApi obj = rest.callGetAllAuth();
        if (obj.getCodeHttp() == 200) {
            lstDtes = (List<DtesResponse>) obj.getBody();
        }
    }

    public void showDlgDetToInvalidate() {

        ResponseDto response = invalidateService.findDteToInvalidate(idFactura, codigoDte, codigoGeneracion, sessionService.getToken());

        if (response.getStatusCode() == 0) {
            DteToInvalidate dte = (DteToInvalidate) response.getBody();

            DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                    .draggable(false)
                    .resizable(false)
                    .maximizable(false)
                    .modal(true)
                    .width("700")
                    .build();
            PrimeFaces.current().dialog().openDynamic("process/invoce/dialog/dlg-invalidar-dte", options, null);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dteInv", dte);
        } else {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title("ALERTA")
                    .message(response.getBody().toString())
                    .build().showMessage();
        }

    }

    public void onDteInvalidate(SelectEvent event) {
        findDtes();
    }
}
