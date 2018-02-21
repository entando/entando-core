package org.entando.entando.aps.system.services;

import java.util.ArrayList;
import java.util.List;

public abstract class DtoBuilder<I, O> implements IDtoBuilder<I, O> {

    @Override
    public O convert(I entity) {
        O dto = null;
        if (null != entity) {

            dto = toDto(entity);
        }
        return dto;
    }

    @Override
    public List<O> convert(List<I> list) {
        if (null == list) {
            return null;
        }
        List<O> dto = new ArrayList<>();
        for (I entry : list) {
            dto.add(convert(entry));
        }
        return dto;
    }

    protected abstract O toDto(I src);

}
