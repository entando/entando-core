package org.entando.entando.aps.system.services.pagemodel;

import java.util.List;

public interface PageModelServiceUtilizer<T> {

    public String getManagerName();

    public List<T> getPageModelUtilizer(String pageModelCode);
}
