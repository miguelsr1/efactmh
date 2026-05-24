package sv.com.jsoft.efactmh.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private String title;
    private int status;
    private List<Violation> violations;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Violation {

        private String field;
        private String message;

    }
}
