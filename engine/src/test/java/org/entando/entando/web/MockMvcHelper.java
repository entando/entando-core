package org.entando.entando.web;

import com.ctc.wstx.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

public class MockMvcHelper {

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;
    private String accessToken;

    public MockMvcHelper(MockMvc mvc) {
        this.mvc = mvc;
    }

    public MockMvcHelper(MockMvc mvc, String accessToken) {
        this.mvc = mvc;
        this.accessToken = accessToken;
    }

    public MockMvc getMvc() {
        return mvc;
    }

    public MockMvcHelper setMvc(MockMvc mvc) {
        this.mvc = mvc;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public MockMvcHelper setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /***********************************************************************************************************
     * GET
     ***********************************************************************************************************/


    /**
     * returns a ResultActions from the MockMcv call in get with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     */
    public ResultActions getMockMvc(String url) throws Exception {

        return this.getMockMvc(url, null);
    }

    /**
     * returns a ResultActions from the MockMcv call in get with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     * @para contentBody the body as string to attach to the request
     */
    public ResultActions getMockMvc(String url, Object contentBody, String... pathParams) throws Exception {

        return this.executeRequest(MockMvcRequestBuilders::get, url, contentBody, pathParams);
    }


    /***********************************************************************************************************
     * POST
     ***********************************************************************************************************/


    /**
     * returns a ResultActions from the MockMcv call in post with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     */
    public ResultActions postMockMvc(String url) throws Exception {

        return this.postMockMvc(url, null);
    }

    /**
     * returns a ResultActions from the MockMcv call in post with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     * @para contentBody the body as string to attach to the request
     */
    public ResultActions postMockMvc(String url, Object contentBody, String... pathParams) throws Exception {

        return this.executeRequest(MockMvcRequestBuilders::post, url, contentBody, pathParams);
    }


    /***********************************************************************************************************
     * PUT
     ***********************************************************************************************************/


    /**
     * returns a ResultActions from the MockMcv call in put with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     */
    public ResultActions putMockMvc(String url) throws Exception {

        return this.putMockMvc(url, null);
    }


    /**
     * returns a ResultActions from the MockMcv call in put with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     * @para contentBody the body as string to attach to the request
     */
    public ResultActions putMockMvc(String url, Object contentBody, String... pathParams) throws Exception {

        return this.executeRequest(MockMvcRequestBuilders::put, url, contentBody, pathParams);
    }


    /***********************************************************************************************************
     * DELETE
     ***********************************************************************************************************/


    /**
     * returns a ResultActions from the MockMcv call in delete with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     */
    public ResultActions deleteMockMvc(String url) throws Exception {

        return this.deleteMockMvc(url, null);
    }

    /**
     * returns a ResultActions from the MockMcv call in delete with the url received, the test jwt and the JSON UTF8 as media type
     *
     * @param url the url to call
     * @return the configured MockMvc
     * @throws Exception
     * @para contentBody the body as string to attach to the request
     */
    public ResultActions deleteMockMvc(String url, Object contentBody, String... pathParams) throws Exception {

        return this.executeRequest(MockMvcRequestBuilders::delete, url, contentBody, pathParams);
    }


    /***********************************************************************************************************
     * COMMON
     ***********************************************************************************************************/


    /**
     * receives a MockHttpServletRequestBuilder, puts standard headers into it and returns it
     *
     * @param requestBuilder the MockHttpServletRequestBuilder to which add headers
     * @return the MockHttpServletRequestBuilder with standard headers
     */
    private MockHttpServletRequestBuilder addStandardHeaders(MockHttpServletRequestBuilder requestBuilder) {

        if (! StringUtils.isEmpty(this.accessToken)) {
            requestBuilder = requestBuilder.header("Authorization", "Bearer " + this.accessToken);
        }

        return requestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * receives a MockHttpServletRequestBuilder and an Object, puts the string representation of the object into the MockHttpServletRequestBuilder
     *
     * @param contentBody    the object to stringify and write into the request builder
     * @param requestBuilder the MockHttpServletRequestBuilder to which add headers
     * @return the MockHttpServletRequestBuilder with standard headers
     */
    private MockHttpServletRequestBuilder addBodyContent(MockHttpServletRequestBuilder requestBuilder, Object contentBody) throws JsonProcessingException {

        if (null != contentBody) {
            requestBuilder.content(this.objectMapper.writeValueAsString(contentBody));
        }

        return requestBuilder;
    }


    /**
     * executes an http request and returns its result
     *
     * @param httpRequestExecutor the function containing the execution logic (mainly regarding http method)
     * @param url                 the url to call
     * @param contentBody         the optional content body to add to the request
     * @param pathParams          the optional string path params
     * @return a ResultActions containing the result of the http call
     * @throws Exception
     */
    private ResultActions executeRequest(HttpRequestExecutor<String, MockHttpServletRequestBuilder> httpRequestExecutor, String url, Object contentBody, String... pathParams) throws Exception {

        MockHttpServletRequestBuilder reqBuilder = this.addStandardHeaders(httpRequestExecutor.request(url, pathParams));
        this.addBodyContent(reqBuilder, contentBody);

        return this.mvc.perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     *
     * @param <String>
     * @param <MockHttpServletRequestBuilder>
     */
    @FunctionalInterface
    interface HttpRequestExecutor<String, MockHttpServletRequestBuilder> {

        MockHttpServletRequestBuilder request(String url, String... pathParams);
    }

}