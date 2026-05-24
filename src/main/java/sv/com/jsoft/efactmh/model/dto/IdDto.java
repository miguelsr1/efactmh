package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IdDto implements Serializable {

    private Long id;
}
