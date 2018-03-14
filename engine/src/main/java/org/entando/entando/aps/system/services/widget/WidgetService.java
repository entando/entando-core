package org.entando.entando.aps.system.services.widget;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widget.model.WidgetDto;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.entando.entando.web.widget.validator.WidgetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class WidgetService implements IWidgetService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IWidgetTypeManager widgetManager;

    @Autowired
    private IGuiFragmentManager guiFragmentManager;

    @Autowired
    private IDtoBuilder<WidgetType, WidgetDto> dtoBuilder;

    @Override
    public WidgetDto getWidget(String widgetCode) {
        WidgetType widgetType = widgetManager.getWidgetType(widgetCode);
        WidgetDto widgetDto = dtoBuilder.convert(widgetType);

        try {
            addFragments(widgetDto);
        }catch(Exception e){
            logger.error("Failed to fetch gui fragment for widget type code ",e);
        }
        return widgetDto;
    }

    @Override
    public WidgetDto addWidget(WidgetRequest widgetRequest) {

        WidgetType widgetType = this.createWidget(widgetRequest);

        try {
            widgetManager.addWidgetType(widgetType);
        }catch(ApsSystemException e) {
            logger.error("Failed to add widget type for request {} ",widgetRequest);
            throw new RestServerError("error in update group", e);
        }

        WidgetDto widgetDto = this.dtoBuilder.convert(widgetType);
        return widgetDto;
    }

    @Override
    public void removeWidget(String widgetCode) {

        WidgetType type = widgetManager.getWidgetType(widgetCode);
        try {
            BeanPropertyBindingResult validationResult = checkWidgetForDelete(type);

            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }

            this.widgetManager.deleteWidgetType(widgetCode);
        }catch(ApsSystemException e) {
            logger.error("Failed to remove widget type for request {} ",widgetCode);
            throw new RestServerError("failed to update widget type by code ", e);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public PagedMetadata<WidgetDto> getWidgets(RestListRequest restListReq) {

        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(restListReq.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(WidgetDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<WidgetType> widgets = this.widgetManager.getWidgetTypes(filters);
            List<WidgetDto> dtoList = dtoBuilder.convert(widgets.getList());

            for(WidgetDto widgetDto: dtoList) {
                addFragments(widgetDto);
            }

            PagedMetadata<WidgetDto> pagedMetadata = new PagedMetadata<>(restListReq, widgets);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in get widgets", t);
            throw new RestServerError("error in get widgets", t);
        }
    }

    @Override
    public WidgetDto updateWidget(String widgetCode, WidgetRequest widgetRequest) {

        WidgetType type = this.widgetManager.getWidgetType(widgetCode);

        if(type == null) {
            throw new RestRourceNotFoundException(null, "widget", widgetCode);
        }

        processWidgetType(type, widgetRequest);
        WidgetDto widgetDto = dtoBuilder.convert(type);

        try {
            widgetManager.updateWidgetType(widgetCode, type.getTitles(), type.getConfig(), type.getMainGroup());

            addFragments(widgetDto);
        }catch(Throwable e) {
            logger.error("failed to update widget type", e);
            throw new RestServerError("Failed to update widget", e);
        }

        return widgetDto;
    }

    private WidgetType createWidget(WidgetRequest widgetRequest) {

        WidgetType type = new WidgetType();
        processWidgetType(type, widgetRequest);

        return type;
    }

    private void processWidgetType(WidgetType type, WidgetRequest widgetRequest) {
        type.setCode(widgetRequest.getCode());
        type.setLocked(widgetRequest.getUsed());

        ApsProperties titles = new ApsProperties();
        widgetRequest.getTitles().forEach((k,v) -> titles.put(k, v));
        type.setTitles(titles);
        type.setPluginCode(widgetRequest.getPluginCode());
        type.setMainGroup(widgetRequest.getGroup());

    }

    private void addFragments(WidgetDto widgetDto) throws Exception{
        List<String> fragmentCodes = guiFragmentManager.getGuiFragmentCodesByWidgetType(widgetDto.getCode());

        if (fragmentCodes != null) {
            for (String fragmentCode : fragmentCodes) {
                GuiFragment fragment = guiFragmentManager.getGuiFragment(fragmentCode);
                widgetDto.addGuiFragmentRef(fragment.getCode(), fragment.getCurrentGui(), fragment.getDefaultGui());
            }
        }
    }
    public IWidgetTypeManager getWidgetManager() {
        return widgetManager;
    }

    public void setWidgetManager(IWidgetTypeManager widgetManager) {
        this.widgetManager = widgetManager;
    }

    public IDtoBuilder<WidgetType, WidgetDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<WidgetType, WidgetDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    private BeanPropertyBindingResult checkWidgetForDelete(WidgetType widgetType) throws ApsSystemException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(widgetType, "widget");

        if (null == widgetType) {
            return bindingResult;
        }

        if(widgetType.isLocked()) {
            bindingResult.reject(WidgetValidator.ERRCODE_CANNOT_DELETE_USED_WIDGET, new String[]{widgetType.getCode()}, widgetType.getCode() + " cannot be deleted because it is referenced in use");
        }

        return bindingResult;
    }
}
