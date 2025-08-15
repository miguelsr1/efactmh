package sv.com.jsoft.efactmh.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author msanchez
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ResponseRestApi<T> {

    private int codeHttp;
    private T body;
}
