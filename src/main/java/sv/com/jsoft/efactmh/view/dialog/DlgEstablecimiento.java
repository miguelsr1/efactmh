package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@RequestScoped
@Named
@Slf4j
public class DlgEstablecimiento implements Serializable {
    
    @Getter
    @Setter
    private EstablecimientoDto establecimientoDto;
    @Inject
    SessionService sessionService;
    
    @PostConstruct
    public void init(){
        establecimientoDto = new EstablecimientoDto();
        establecimientoDto.setActivo(Boolean.TRUE);
    }
    
    public void addEstable(){
        
        saveEstable();
        
        PrimeFaces.current().dialog().closeDynamic(establecimientoDto);
    }
    
    private void saveEstable(){
         RestUtil rest = RestUtil
                    .builder()
                    .clazz(IdDto.class)
                    .jwtDto(sessionService.getToken())
                    .body(establecimientoDto)
                    .endpoint("/api/establecimiento").build();
         
         IdDto idDto = (IdDto) rest.callPostAuth();
         log.info("ESTABLECIMIENTO CREADO:" + idDto);
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
