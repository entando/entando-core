package org.entando.entando.aps.system.services.pagemodel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.pagemodel.model.FrameDto;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PageModelDtoTest {

    @Test
    public void test_json() throws JsonProcessingException {

        FrameDto frameDto = new FrameDto();
        frameDto.setPos(0);
        frameDto.setDescription("description");

        PageModelDto dto = new PageModelDto();
        dto.setCode("code");
        dto.setDescription("description");
        dto.getConfiguration().getFrames().add(frameDto);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

        String actual = "{\n" +
                        "  \"code\" : \"code\",\n" +
                        "  \"description\" : \"description\",\n" +
                        "  \"mainFrame\" : -1,\n" +
                        "  \"pluginCode\" : null,\n" +
                        "  \"template\" : null,\n" +
                        "  \"configuration\" : {\n" +
                        "    \"frames\" : [ {\n" +
                        "      \"pos\" : 0,\n" +
                        "      \"description\" : \"description\",\n" +
                        "      \"mainFrame\" : false,\n" +
                        "      \"defaultWidget\" : null,\n" +
                        "      \"sketch\" : null\n" +
                        "    } ]\n" +
                        "  }\n" +
                        "}";
        assertThat(json, is(actual));
    }
}
