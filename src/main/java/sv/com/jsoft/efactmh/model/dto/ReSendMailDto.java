package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

@Data
public class ReSendMailDto {
    private String email;
    private Long idInvoce;
}
