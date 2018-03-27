package org.entando.entando.aps.system.services.group;

import java.util.List;

public interface GroupServiceUtilizer<T> {

    public String getManagerName();

    public List<T> getGroupUtilizer(String groupCode);
}
