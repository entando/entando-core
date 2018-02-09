/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.pagemodel;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Holds the data used to draw the {@link PageModel} layout
 *
 * @author spuddu
 *
 */
@XmlRootElement(name = "sketch")
@XmlType(propOrder = {"x1", "y1", "x2", "y2"})
public class FrameSketch implements Serializable {

	public void setCoords(int x1, int y1, int x2, int y2) {
		this.setX1(x1);
		this.setY1(y1);
		this.setX2(x2);
		this.setY2(y2);
	}

	@XmlElement(name = "x1", required = true)
	public int getX1() {
		return _x1;
	}

	public void setX1(int x1) {
		this._x1 = x1;
	}

	@XmlElement(name = "y1", required = true)
	public int getY1() {
		return _y1;
	}

	public void setY1(int y1) {
		this._y1 = y1;
	}

	@XmlElement(name = "x2", required = true)
	public int getX2() {
		return _x2;
	}

	public void setX2(int x2) {
		this._x2 = x2;
	}

	@XmlElement(name = "y2", required = true)
	public int getY2() {
		return _y2;
	}

	public void setY2(int y2) {
		this._y2 = y2;
	}

	private int _x1;
	private int _y1;
	private int _x2;
	private int _y2;

}
