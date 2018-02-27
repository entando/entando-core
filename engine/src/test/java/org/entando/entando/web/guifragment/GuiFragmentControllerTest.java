/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.guifragment;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GuiFragmentControllerTest extends AbstractControllerTest {

	@Mock
	private GuiFragmentService guiFragmentService;

	@InjectMocks
	private GuiFragmentController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.addInterceptors(entandoOauth2Interceptor)
				.setHandlerExceptionResolvers(createHandlerExceptionResolver())
				.build();
	}

	@Test
	public void should_load_the_list_of_fragments() throws Exception {
		UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
		String accessToken = mockOAuthInterceptor(user);
		/*
		String mockJsonResult = "{\n"
				+ "  \"page\" : 1,\n"
				+ "  \"size\" : 2,\n"
				+ "  \"last\" : 1,\n"
				+ "  \"count\" : 6,\n"
				+ "  \"body\" : [ {\n"
				+ "    \"code\" : \"helpdesk\",\n"
				+ "    \"name\" : \"Helpdesk\"\n"
				+ "  }, {\n"
				+ "    \"code\" : \"management\",\n"
				+ "    \"name\" : \"Management\"\n"
				+ "  } ]\n"
				+ "}";
		PagedMetadata<GroupDto> mockResult = (PagedMetadata<GroupDto>) this.createPagedMetadata(mockJsonResult);
		when(groupService.getGroups(any(RestListRequest.class))).thenReturn(mockResult);
		 */
		ResultActions result = mockMvc.perform(
				get("/fragments")
				.param("pageNum", "1")
				.param("pageSize", "4")
				.header("Authorization", "Bearer " + accessToken)
		);
		result.andExpect(status().isOk());
		RestListRequest restListReq = new RestListRequest();
		restListReq.setPageNum(1);
		restListReq.setPageSize(4);
		Mockito.verify(guiFragmentService, Mockito.times(1)).getGuiFragments(restListReq);
	}

}
