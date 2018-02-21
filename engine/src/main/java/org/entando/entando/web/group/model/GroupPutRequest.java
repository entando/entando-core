package org.entando.entando.web.group.model;

import javax.validation.constraints.NotNull;

/**
 * rappresenta il payload per l'update (put)
 *
 */
public class GroupPutRequest {


	@NotNull(message = "NotBlank.group.descr")
	private String descr;

	public GroupPutRequest() {

	}

	public GroupPutRequest(String descr) {
		this.descr = descr;
	}


	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}
