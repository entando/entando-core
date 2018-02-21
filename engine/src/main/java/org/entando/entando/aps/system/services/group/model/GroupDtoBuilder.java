package org.entando.entando.aps.system.services.group.model;

import com.agiletec.aps.system.services.group.Group;
import org.entando.entando.aps.system.services.DtoBuilder;


public class GroupDtoBuilder extends DtoBuilder<Group, GroupDto> {

    @Override
    protected GroupDto toDto(Group src) {
        GroupDto dest = new GroupDto();
        dest.setCode(src.getName());
        dest.setName(src.getDescription());
        return dest;
    }

}
