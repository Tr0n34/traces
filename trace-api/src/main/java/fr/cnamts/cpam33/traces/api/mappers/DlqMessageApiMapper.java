package fr.cnamts.cpam33.traces.api.mappers;

import fr.cnamts.cpam33.traces.api.dto.DlqMessageDetailDto;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageSummaryDto;
import fr.cnamts.cpam33.traces.api.entities.DlqMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface DlqMessageApiMapper {

    DlqMessageSummaryDto toSummaryDto(DlqMessageEntity entity);

    @Mapping(target = "payloadBase64", expression = "java(toBase64(entity.getPayload()))")
    DlqMessageDetailDto toDetailDto(DlqMessageEntity entity);

    default String toBase64(byte[] payload) {
        return Base64.getEncoder().encodeToString(payload);
    }

}
