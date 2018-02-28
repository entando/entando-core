package org.entando.entando.web.pagemodel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                                   "classpath:spring/propertyPlaceholder.xml",
                                   "classpath*:spring/baseSystemConfig.xml",


})
//@WebAppConfiguration
public class PageModelControllerIntegrationTest {



    @Before
    public void setUp() throws Exception {

        TestEntandoJndiUtils.setupJndi();

    }

    @Test
    public void testGetList() {

        assertThat(true, is(true));
    }



}
