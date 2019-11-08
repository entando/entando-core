package org.entando.entando.web.dataobjectmodel;

import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DataModelDtoToRequestConverter implements Converter<DataModelDto, DataObjectModelRequest> {

    @Override
    public DataObjectModelRequest convert(DataModelDto source) {
        DataObjectModelRequest request = new DataObjectModelRequest();
        request.setModelId(source.getModelId());
        request.setModel(source.getModel());
        request.setDescr(source.getDescr());
        request.setType(source.getType());
        request.setStylesheet(source.getStylesheet());
        return request;
    }
}
