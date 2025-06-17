package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class JwtDto implements Serializable {

    private String accessToken;
    private int expiresIn;
    private int refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private int notBeforePolicy;
    private String sessionState;
    private String scope;
}
