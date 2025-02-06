package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.EmisorDto;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.model.mapper.EmisorMapper;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.services.SecurityService;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class EmisorView implements Serializable {

    @Getter
    @Setter
    private EstablecimientoDto estable = new EstablecimientoDto();
    @Getter
    @Setter
    private List<EstablecimientoDto> lstEstable = new ArrayList<>();

    @Getter
    @Setter
    private Emisor emisor;
    @Getter
    @Setter
    private String codDepa;
    @Getter
    @Setter
    private Integer idMunicipio;
    @Getter
    private List<CatalogoDto> lstDepartamentos;
    @Getter
    private List<MunicipioDto> lstMunicipios;
    @Getter
    private List<CatalogoDto> lstGiros;

    @Inject
    CatalogoService catalogoService;
    @Inject
    SecurityService securityService;

    @PostConstruct
    public void init() {
        lstGiros = catalogoService.getLstGiros();
        loadDataEmisor();
    }

    private void loadDataEmisor() {
        RestUtil rest = RestUtil.builder().clazz(Emisor.class).endpoint("/api/secured/emisor").build();
        emisor = (Emisor) rest
                .callGetOne(securityService.getToken());

        codDepa = emisor.getCodigoDepartamento();
        lstMunicipios = catalogoService.getMunicipioDtoByCodDepa(emisor.getCodigoDepartamento());
        lstDepartamentos = catalogoService.getLstDepartamentos();
    }
    
    public void updateListaMunicipios(){
        lstMunicipios = catalogoService.getMunicipioDtoByCodDepa(codDepa);
    }

    public void agregarEstablecimiento() {
        lstEstable.add(estable);
        estable = new EstablecimientoDto();
    }
    
    public void guardar(){
        EmisorDto emisorDto = EmisorMapper.INSTANCE.toDto(emisor);
        RestUtil rest = RestUtil
                .builder()
                .endpoint("/api/secured/emisor/"+emisor.getIdContribuyente()).build();
        
        rest.callPutAuth(securityService.getToken(), emisorDto);
        /*emisorDto.setActivo(emisor.getActivo());
        emisorDto.setCodigoActividad(emisor.getCodigoActividad());
        emisorDto.setCodigoEstablecimiento(emisor.getCodigoEstablecimiento());
        emisorDto.setCorreo(emisor.getCorreo());
        emisorDto.setDireccion(emisor.getDireccion());
        emisorDto.setIdMunicipio(emisor.getIdMunicipio());
        emisorDto.setNit(emisor.getNit());
        emisorDto.setNombreComercial(emisor.getNombreComercial());
        emisorDto.setNrc(emisor.getNrc());
        emisorDto.setRazonSocial(emisor.getRazonSocial());
        emisorDto.setTelefono(emisor.getTelefono());
        emisorDto.setUsuario(emisor.getUsuario());*/
    }
    
    public String cancelar(){
        return "/app/home";
    }
}
