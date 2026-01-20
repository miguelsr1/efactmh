package sv.com.jsoft.efactmh.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import sv.com.jsoft.efactmh.model.dto.ErrorMessageDto;

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
    private ErrorMessageDto errorMessageDto;

    public ResponseRestApi(int codeHttp, T body) {
        this.codeHttp = codeHttp;
        this.body = body;
    }

}
