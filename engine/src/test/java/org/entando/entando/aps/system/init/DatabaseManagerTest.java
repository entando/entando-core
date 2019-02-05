package org.entando.entando.aps.system.init;

import java.io.IOException;

import javax.sql.DataSource;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.init.cache.IInitializerManagerCacheWrapper;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.init.util.ComponentUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ListableBeanFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class DatabaseManagerTest {

    @Mock
    private IInitializerManagerCacheWrapper cacheWrapper;

    @Mock
    private ListableBeanFactory beanFactory;

    @InjectMocks
    private DatabaseManager databaseManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_uninstall_component() throws ApsSystemException, IOException {
        Component component = ComponentUtils.getEntandoComponent("test_component");

        when(beanFactory.getBeanNamesForType(DataSource.class)).thenReturn(new String[]{"portDataSource", "servDataSource"});

        SystemInstallationReport systemInstallationReport = spy(createMockReport());
        databaseManager.uninstallComponentResources(component, systemInstallationReport);
        verify(systemInstallationReport, times(1)).removeComponentReport("test_component");

    }


    private SystemInstallationReport createMockReport() {
        return new SystemInstallationReport(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                       "<reports status=\"OK\">\n"+
                       "\t<creation>2019-02-04 13:52:01</creation>\n"+
                       "\t<lastupdate>2019-02-04 14:22:43</lastupdate>\n"+
                       "\t<components>\n"+
                       "\t\t<component code=\"test_component\" date=\"2019-02-04 13:52:02\" status=\"OK\">\n"+
                       "\t\t\t<schema status=\"OK\">\n"+
                       "\t\t\t\t<datasource name=\"portDataSource\" status=\"NOT_AVAILABLE\" />\n"+
                       "\t\t\t\t<datasource name=\"servDataSource\" status=\"NOT_AVAILABLE\" />\n"+
                       "\t\t\t</schema>\n"+
                       "\t\t\t<data status=\"OK\">\n"+
                       "\t\t\t\t<datasource name=\"portDataSource\" status=\"NOT_AVAILABLE\" />\n"+
                       "\t\t\t\t<datasource name=\"servDataSource\" status=\"NOT_AVAILABLE\" />\n"+
                       "\t\t\t</data>\n"+
                       "\t\t\t<postProcess status=\"NOT_AVAILABLE\" />\n"+
                       "\t\t</component>\n"+
                       "\t</components>\n"+
                       "</reports>\n");
    }

}
