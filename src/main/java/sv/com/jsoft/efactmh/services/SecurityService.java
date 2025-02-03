package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.JwtDto;

/**
 *
 * @author migue
 */
@SessionScoped
@Named
public class SecurityService implements Serializable {

    @Getter
    @Setter
    private JwtDto token;
}
