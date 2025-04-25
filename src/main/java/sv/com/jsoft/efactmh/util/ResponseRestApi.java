package sv.com.jsoft.efactmh.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
public class ResponseRestApi {
    private int codeHttp;
    private Object body;
}