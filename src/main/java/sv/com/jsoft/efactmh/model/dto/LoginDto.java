package sv.com.jsoft.efactmh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto implements Serializable {

    private String username;
    private String password;
}
