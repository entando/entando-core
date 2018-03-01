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
package org.entando.entando.web.pagemodel.validator;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.pagemodel.model.PageModelFrameReq;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class PageModelValidator implements Validator {

    public static final String ERRCODE_FRAMES_POS_MISMATCH = "1";
    public static final String ERRCODE_CODE_EXISTS = "2";
    public static final String ERRCODE_PAGEMODEL_REFERENCES = "3";
    public static final String ERRCODE_URINAME_MISMATCH = "4";;


    @Override
    public boolean supports(Class<?> paramClass) {
        return PageModelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PageModelRequest request = (PageModelRequest) target;
        this.validateConfiguration(request, errors);
    }

    private void validateConfiguration(PageModelRequest request, Errors errors) {
        List<PageModelFrameReq> conf = request.getConfiguration().getFrames();
        if (null == conf || conf.isEmpty()) {
            return;
        }
        //frame positions should start from 0 and be progressive
        List<Integer> positions = conf
                                      .stream()
                                      .sorted((e1, e2) -> Integer.compare(e1.getPos(), e2.getPos()))
                                      .map(i -> i.getPos()).collect(Collectors.toList());
        int firstPosition = positions.get(0);
        if (firstPosition != 0) {
            errors.reject(ERRCODE_FRAMES_POS_MISMATCH, new String[]{}, "pageModel.frames.pos.mismatch");
            return;
        }

        int lastPosition = positions.get(positions.size() - 1);
        if (lastPosition != positions.size() - 1) {
            errors.reject(ERRCODE_FRAMES_POS_MISMATCH, new String[]{}, "pageModel.frames.pos.mismatch");
            return;
        }
        if (positions.size() != new HashSet<Integer>(positions).size()) {
            errors.reject(ERRCODE_FRAMES_POS_MISMATCH, new String[]{}, "pageModel.frames.pos.mismatch");
            return;
        }
    }

    public void validateBodyName(String code, PageModelRequest pageModelRequest, Errors errors) {

        if (!StringUtils.equals(code, pageModelRequest.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{code, pageModelRequest.getCode()}, "pageModel.code.mismatch");
        }

    }


}
