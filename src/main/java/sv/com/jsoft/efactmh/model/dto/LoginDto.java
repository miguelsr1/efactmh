package sv.com.jsoft.efactmh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
public class LoginDto {

    private String username;
    private String password;
}
