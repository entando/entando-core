package org.entando.entando.aps.system.services;

import java.util.List;

public interface IDtoBuilder<I, O> {

    O convert(I entity);

    List<O> convert(List<I> list);

}
