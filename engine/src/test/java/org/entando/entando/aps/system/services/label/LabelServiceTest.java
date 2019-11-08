package org.entando.entando.aps.system.services.label;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.I18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.*;

public class LabelServiceTest {

    @Mock private I18nManager i18nManager;
    @Mock private ILangManager langManager;

    private LabelService labelService;
    private Lang lang;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        labelService = new LabelService();
        labelService.setI18nManager(i18nManager);
        labelService.setLangManager(langManager);
        lang = new Lang();
        lang.setCode("EN");
    }

    @Test
    public void testGetLabelGroupsFilteringEqual() {
        RestListRequest request = new RestListRequest();
        Filter filter = new Filter("value", "some_value", FilterOperator.EQUAL.getValue());

        when(i18nManager.getLabelGroups()).thenReturn(singletonMap("EN", create(singletonMap("EN", "some_value"))));

        request.setFilters(new Filter [] { filter });
        PagedMetadata<LabelDto> labelGroups = labelService.getLabelGroups(request);

        assertThat(labelGroups.getBody()).hasSize(1);
        assertThat(labelGroups.getBody().get(0).getKey()).isEqualTo("EN");
        assertThat(labelGroups.getBody().get(0).getTitles().get("EN")).isEqualTo("some_value");

        verify(i18nManager, times(1)).getLabelGroups();

        reset(i18nManager);
        when(i18nManager.getLabelGroups()).thenReturn(singletonMap("EN", create(singletonMap("EN", "some_value"))));

        filter = new Filter("value", "some_", FilterOperator.EQUAL.getValue());
        request.setFilters(new Filter [] { filter });

        labelGroups = labelService.getLabelGroups(request);

        assertThat(labelGroups.getBody()).hasSize(0);
    }

    @Test
    public void testGetLabelGroupsFilteringLike() {
        RestListRequest request = new RestListRequest();
        Filter filter = new Filter("value", "some_value", FilterOperator.LIKE.getValue());

        when(i18nManager.getLabelGroups()).thenReturn(singletonMap("EN", create(singletonMap("EN", "some_value"))));

        request.setFilters(new Filter [] { filter });
        PagedMetadata<LabelDto> labelGroups = labelService.getLabelGroups(request);

        assertThat(labelGroups.getBody()).hasSize(1);
        assertThat(labelGroups.getBody().get(0).getKey()).isEqualTo("EN");
        assertThat(labelGroups.getBody().get(0).getTitles()).containsOnly(entry("EN", "some_value"));

        verify(i18nManager, times(1)).getLabelGroups();

        reset(i18nManager);
        when(i18nManager.getLabelGroups()).thenReturn(singletonMap("EN", create(singletonMap("EN", "some_value"))));

        filter = new Filter("value", "some_", FilterOperator.LIKE.getValue());
        request.setFilters(new Filter [] { filter });

        labelGroups = labelService.getLabelGroups(request);

        assertThat(labelGroups.getBody()).hasSize(1);
        assertThat(labelGroups.getBody().get(0).getKey()).isEqualTo("EN");
        assertThat(labelGroups.getBody().get(0).getTitles()).containsOnly(entry("EN", "some_value"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetLabelGroupNotFound() throws ApsSystemException {
        when(i18nManager.getLabelGroup(eq("not_found"))).thenReturn(null);
        labelService.getLabelGroup("not_found");
        verify(i18nManager, times(1)).getLabelGroup(eq("not_found"));
    }

    @Test
    public void testGetLabelGroup() throws ApsSystemException {
        when(i18nManager.getLabelGroup(eq("lab"))).thenReturn(create(singletonMap("EN", "some_value")));
        final LabelDto label = labelService.getLabelGroup("lab");
        assertThat(label.getKey()).isEqualTo("lab");
        assertThat(label.getTitles()).hasSize(1);
        assertThat(label.getTitles()).containsOnly(entry("EN", "some_value"));
        verify(i18nManager, times(1)).getLabelGroup(eq("lab"));
    }

    @Test(expected = RestServerError.class)
    public void testAddLabelGroupError() throws ApsSystemException {
        when(langManager.getDefaultLang()).thenReturn(lang);
        when(langManager.getAssignableLangs()).thenReturn(singletonList(lang));
        when(langManager.getLangs()).thenReturn(singletonList(lang));

        doThrow(ApsSystemException.class).when(i18nManager).addLabelGroup(anyString(), any(ApsProperties.class));

        final LabelDto label = new LabelDto("lab", singletonMap("EN", "some_value"));
        labelService.addLabelGroup(label);
    }

    @Test(expected = ValidationConflictException.class)
    public void testAddLabelGroupNotAssignableLang() throws ApsSystemException {
        when(langManager.getDefaultLang()).thenReturn(lang);
        when(langManager.getAssignableLangs()).thenReturn(Collections.emptyList());
        when(langManager.getLangs()).thenReturn(singletonList(lang));

        final LabelDto label = new LabelDto("lab", singletonMap("EN", "some_value"));
        labelService.addLabelGroup(label);
    }

    @Test
    public void testAddLabelGroup() throws ApsSystemException {
        when(langManager.getDefaultLang()).thenReturn(lang);
        when(langManager.getAssignableLangs()).thenReturn(singletonList(lang));
        when(langManager.getLangs()).thenReturn(singletonList(lang));

        final LabelDto label = new LabelDto("lab", singletonMap("EN", "some_value"));
        final LabelDto labelResult = labelService.addLabelGroup(label);
        ArgumentCaptor<ApsProperties> captor = ArgumentCaptor.forClass(ApsProperties.class);
        verify(i18nManager, times(1)).addLabelGroup(eq("lab"), captor.capture());

        final ApsProperties value = captor.getValue();
        assertThat(value.getProperty("EN")).isEqualTo("some_value");

        assertThat(labelResult.getKey()).isEqualTo("lab");
        assertThat(labelResult.getTitles()).hasSize(1);
        assertThat(labelResult.getTitles()).containsOnly(entry("EN", "some_value"));
    }

    @Test
    public void testUpdateLabelGroup() throws ApsSystemException {
        when(langManager.getDefaultLang()).thenReturn(lang);
        when(langManager.getAssignableLangs()).thenReturn(singletonList(lang));
        when(langManager.getLangs()).thenReturn(singletonList(lang));

        when(i18nManager.getLabelGroup(eq("lab"))).thenReturn(create(singletonMap("EN", "some_value")));

        final LabelDto label = new LabelDto("lab", singletonMap("EN", "another_value"));
        final LabelDto labelResult = labelService.updateLabelGroup(label);
        ArgumentCaptor<ApsProperties> captor = ArgumentCaptor.forClass(ApsProperties.class);

        verify(i18nManager, times(1)).getLabelGroup(eq("lab"));
        verify(i18nManager, times(1)).updateLabelGroup(eq("lab"), captor.capture());

        final ApsProperties value = captor.getValue();
        assertThat(value.getProperty("EN")).isEqualTo("another_value");

        assertThat(labelResult.getKey()).isEqualTo("lab");
        assertThat(labelResult.getTitles()).hasSize(1);
        assertThat(labelResult.getTitles()).containsOnly(entry("EN", "another_value"));
    }

    private ApsProperties create(final Map<String, String> value) {
        final ApsProperties properties = new ApsProperties();
        value.keySet().forEach(item -> properties.setProperty(item, value.get(item)));
        return properties;
    }

}