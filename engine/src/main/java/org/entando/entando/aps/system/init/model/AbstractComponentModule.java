/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.agiletec.aps.system.exception.ApsSystemException;

public class AbstractComponentModule implements ComponentModule {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentModule.class);

    private Map<String, String> sqlResourcesPaths = new HashMap<>();

    private List<IPostProcess> postProcesses;

    protected void extractSqlResources(Element sqlResourcesElement) throws ApsSystemException {
        if (null != sqlResourcesElement) {
            List<Element> datasourceElements = sqlResourcesElement.getChildren("datasource");
            for (int j = 0; j < datasourceElements.size(); j++) {
                Element datasourceElement = datasourceElements.get(j);
                String datasourceName = datasourceElement.getAttributeValue("name");
                String sqlResourcePath = datasourceElement.getText().trim();
                this.getSqlResourcesPaths().put(datasourceName, sqlResourcePath);
            }
        }
    }

    protected void createPostProcesses(Element postProcessesElement, Map<String, String> postProcessClasses) throws ApsSystemException {
        if (null != postProcessesElement) {
            List<Element> postProcessElements = postProcessesElement.getChildren();
            if (null != postProcessElements && !postProcessElements.isEmpty()) {
                for (int i = 0; i < postProcessElements.size(); i++) {
                    Element postProcessElement = postProcessElements.get(i);
                    this.createPostProcess(postProcessElement, postProcessClasses);
                }
            }
        }
    }

    private void createPostProcess(Element postProcessElement, Map<String, String> postProcessClasses) throws ApsSystemException {
        try {
            String name = postProcessElement.getName();
            String className = postProcessClasses.get(name);
            if (null != className) {
                Class postProcessClass = Class.forName(className);
                IPostProcess postProcess = (IPostProcess) postProcessClass.newInstance();
                postProcess.createConfig(postProcessElement);
                if (null == this.getPostProcesses()) {
                    this.setPostProcesses(new ArrayList<IPostProcess>());
                }
                this.getPostProcesses().add(postProcess);
            } else {
                logger.error("Null post process class for process name '{}'", name);
            }
        } catch (Throwable t) {
            logger.error("Error creating Post Process", t);
            throw new ApsSystemException("Error creating Post Process", t);
        }
    }

    @Override
    public Resource getSqlResources(String datasourceName) {
        String path = this.getSqlResourcesPaths().get(datasourceName);
        if (null == path || path.isEmpty()) {
            return null;
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResource(path);
    }

    @Override
    public Map<String, String> getSqlResourcesPaths() {
        return sqlResourcesPaths;
    }

    protected void setSqlResourcesPaths(Map<String, String> sqlResourcesPaths) {
        this.sqlResourcesPaths = sqlResourcesPaths;
    }

    @Override
    public List<IPostProcess> getPostProcesses() {
        return postProcesses;
    }

    protected void setPostProcesses(List<IPostProcess> postProcesses) {
        this.postProcesses = postProcesses;
    }
}
