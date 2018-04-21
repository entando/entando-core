package org.entando.entando.aps.system.services.page;

import java.util.List;

public interface PageServiceUtilizer<T> {

    public String getManagerName();

    public List<T> getPageUtilizer(String pageModelCode);
}
