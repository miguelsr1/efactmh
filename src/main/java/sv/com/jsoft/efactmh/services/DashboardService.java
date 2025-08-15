package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.dto.DashboardDto;
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
}
