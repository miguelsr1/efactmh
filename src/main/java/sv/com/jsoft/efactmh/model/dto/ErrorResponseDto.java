package sv.com.jsoft.efactmh.model.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ErrorResponseDto {

    private String title;
    private int status;
    private List<Violation> violations;

    @Data
    public static class Violation {

        private String field;
        private String message;

    }
}
