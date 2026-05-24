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
public class SendDteRequest implements Serializable {

    private Long idInvoce;
}
