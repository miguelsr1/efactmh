package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author migue
 */
@Data
@ToString
public class ParametroDto {

    private String passwordPrivado;
    private String userJwt;
    private String pwdJwt;
    private String certificado;
    private String ambiente;
    private Boolean activo;
    
    private Boolean test;
    
    public Boolean getTest(){
        return ambiente.compareTo("00") == 0;
    }
}
