package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.dto.DashboardDto;
import sv.com.jsoft.efactmh.model.dto.Invoice7DaysDto;
import sv.com.jsoft.efactmh.model.dto.InvoicedAmountsDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class DashboardService {

    public ResponseRestApi<List<DashboardDto>> findAllData(JwtDto token) {
        ResponseRestApi<List<DashboardDto>> response = RestUtil
                .builder()
                .clazz(DashboardDto.class)
                .jwtDto(token)
                .endpoint("/api/secured/dashboard")
                .build()
                .callGetAllAuth();

        return response;
    }

    public ResponseRestApi<List<Invoice7DaysDto>> getInvoice7Days(JwtDto token) {
        ResponseRestApi<List<Invoice7DaysDto>> response = RestUtil
                .builder()
                .clazz(Invoice7DaysDto.class)
                .jwtDto(token)
                .endpoint("/api/secured/dashboard/invoice-last-7-days")
                .build()
                .callGetAllAuth();

        return response;
    }
    
    public ResponseRestApi<List<InvoicedAmountsDto>> getInvoicedAmounts(JwtDto token) {
        ResponseRestApi<List<InvoicedAmountsDto>> response = RestUtil
                .builder()
                .clazz(InvoicedAmountsDto.class)
                .jwtDto(token)
                .endpoint("/api/secured/dashboard/invoiced-amounts")
                .build()
                .callGetAllAuth();

        return response;
    }
}
