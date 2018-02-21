package org.entando.entando.web.group.model;

import javax.validation.constraints.NotNull;

/**
 * group add payload 
 *
 */
public class GroupRequest extends GroupPutRequest {

	@NotNull(message = "NotBlank.group.name")
	private String name;


	public GroupRequest() {

	}

	public GroupRequest(String name, String descr) {
		super(descr);
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
