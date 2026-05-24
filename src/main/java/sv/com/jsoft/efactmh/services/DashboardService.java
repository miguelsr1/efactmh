package sv.com.jsoft.efactmh.services;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;

import jakarta.inject.Inject;
import sv.com.jsoft.efactmh.model.dto.BalanceDto;
import sv.com.jsoft.efactmh.model.dto.DashboardDto;
import sv.com.jsoft.efactmh.model.dto.Invoice7DaysDto;
import sv.com.jsoft.efactmh.model.dto.InvoicedAmountsDto;
import sv.com.jsoft.efactmh.model.dto.TotalInvoice7DaysDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class DashboardService implements Serializable {

    @Inject
    SessionService sessionService;

    public ResponseRestApi<List<DashboardDto>> findAllData() {
        ResponseRestApi<List<DashboardDto>> response = RestUtil
                .builder()
                .clazz(DashboardDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/dashboard")
                .build()
                .callGetAllAuth();

        return response;
    }

    public ResponseRestApi<List<Invoice7DaysDto>> getInvoice7Days() {
        ResponseRestApi<List<Invoice7DaysDto>> response = RestUtil
                .builder()
                .clazz(Invoice7DaysDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/dashboard/invoice-last-7-days")
                .build()
                .callGetAllAuth();

        return response;
    }
    
    public ResponseRestApi<List<TotalInvoice7DaysDto>> getTotalLast7Days() {
        ResponseRestApi<List<TotalInvoice7DaysDto>> response = RestUtil
                .builder()
                .clazz(TotalInvoice7DaysDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/dashboard/total-last-7-days")
                .build()
                .callGetAllAuth();

        return response;
    }
    
    public ResponseRestApi<List<InvoicedAmountsDto>> getInvoicedAmounts() {
        ResponseRestApi<List<InvoicedAmountsDto>> response = RestUtil
                .builder()
                .clazz(InvoicedAmountsDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/dashboard/invoiced-amounts")
                .build()
                .callGetAllAuth();

        return response;
    }
    
    public ResponseRestApi<BalanceDto> getBalanceDte() {
        ResponseRestApi<BalanceDto> response = RestUtil
                .builder()
                .clazz(BalanceDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/dashboard/balance-dte")
                .build()
                .callGetOneAuth();

        return response;
    }
}
