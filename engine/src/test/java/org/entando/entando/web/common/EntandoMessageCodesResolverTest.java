package org.entando.entando.web.common;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntandoMessageCodesResolverTest {

    @Spy
    private Map<String, String> validationErrorCodeMapping = new HashMap<>();

    @InjectMocks
    private EntandoMessageCodesResolver messageCodesResolver;

    @Before
    public void setUp() throws Exception {
        validationErrorCodeMapping.put("NotNull", "51");
        validationErrorCodeMapping.put("Size", "52");
        validationErrorCodeMapping.put("Min", "53");
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testResolveCode() {
        String[] codes = messageCodesResolver.resolveMessageCodes("Size", "test");
        assertThat(codes[codes.length - 1], is("52"));
    }
}
