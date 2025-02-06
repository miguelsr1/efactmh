package sv.com.jsoft.efactmh.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.dto.EmisorDto;

/**
 *
 * @author migue
 */
@Mapper
public interface EmisorMapper {
    EmisorMapper INSTANCE = Mappers.getMapper(EmisorMapper.class);

    EmisorDto toDto(Emisor emisor);
}
