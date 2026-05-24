package sv.com.jsoft.efactmh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParametroDto implements Serializable {

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
