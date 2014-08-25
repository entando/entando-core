/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jacms.aps.system;

public class JacmsSystemConstants {
	
	/**
	 * Name of the service for contents handling.
	 */
	public static final String CONTENT_MANAGER = "jacmsContentManager";
	
	/**
	 * Name of the service which handles the models of the content
	 */
	public static final String CONTENT_MODEL_MANAGER = "jacmsContentModelManager";
	
	/**
	 * Name of the content rendering service (contents supply system). 
	 */
	public static final String CONTENT_RENDERER_MANAGER = "jacmsBaseContentRenderer";
	
	/**
	 * Name of the service which returns the formatted contents.
	 */
	public static final String CONTENT_DISPENSER_MANAGER = "jacmsBaseContentDispenser";
	
	/**
	 * Name of the service for resources handling.
	 */
	public static final String RESOURCE_MANAGER = "jacmsResourceManager";
	
	/**
	 * Name of the service for symbolic link handling.
	 */
	public static final String LINK_RESOLVER_MANAGER = "jacmsLinkResolverManager";
	
	/**
	 * Name of the search engine service.
	 */
	public static final String SEARCH_ENGINE_MANAGER = "jacmsSearchEngineManager";
	
	/**
	 * Name of the bean of Content Authorization Helper.
	 */
	public static final String CONTENT_AUTHORIZATION_HELPER = "jacmsContentAuthorizationHelper";
	
	/**
	 * Name of the service for handling content page mapping.
	 */
	public static final String CONTENT_PAGE_MAPPER_MANAGER = "jacmsContentPageMapperManager";
	
	public static final String CONTENT_VIEWER_HELPER = "jacmsContentViewerHelper";
	
	public static final String BASE_CONTENT_LIST_HELPER = "jacmsBaseContentListHelper";
	
	public static final String CONTENT_LIST_HELPER = "jacmsContentListHelper";
	
	public static final String CONTENT_PREVIEW_VIEWER_HELPER = "jacmsContentPreviewViewerHelper";
	
	public static final String CONFIG_ITEM_CONTENT_TYPES = "contentTypes";
	
	public static final String CONFIG_ITEM_IMAGE_DIMENSIONS = "imageDimensions";
	
	public static final String CONFIG_ITEM_CONTENT_INDEX_SUB_DIR = "subIndexDir";
	
	/**
	 * Prefix to the key of the object where are stored the public content.
	 * That name must be completed with the ID of the public content. 
	 */
	public static final String CONTENT_CACHE_PREFIX = "jacms_publicContent_";
	
	/**
	 * Prefix to the key of the object where are stored the Content Authorization info.
	 * That name must be completed with the ID of the public content. 
	 */
	public static final String CONTENT_AUTH_INFO_CACHE_PREFIX = "jacms_ContentAuthInfo_";
	
	/**
	 * Prefix to the name of the group where are stored the rendered contents by content id.
	 * That name must be completed with the ID of the content. 
	 */
	public static final String CONTENT_CACHE_GROUP_PREFIX = "jacms_ContentCacheGroup_";
	
	/**
	 * Prefix to the name of the group where are stored the rendered contents by model id.
	 * That name must be completed with the ID of the model. 
	 */
	public static final String CONTENT_MODEL_CACHE_GROUP_PREFIX = "jacms_GroupModelCacheGroup_";
	
	/**
	 * Prefix to the name of the group where are stored the content list by type.
	 * The name must be completed with the ID of the content type.
	 */
	public static final String CONTENTS_ID_CACHE_GROUP_PREFIX = "jacms_ContentsIdCacheGroup_";
	
	/**
	 * Prefix to the name of the group where are stored the contents by type.
	 * The name must be completed with the ID of the content type. 
	 */
	public static final String CONTENT_TYPE_CACHE_GROUP_PREFIX = "jacms_ContentTypeCacheGroup_";
	
	public static final String ATTRIBUTE_ROLE_TITLE = "jacms:title";

	public static final String PERMISSION_EDIT_CONTENTS = "editContents";
	
	public static final String PERMISSION_CONTENT_SUPERVISION = "validateContents";
	
	public static final String CONTENT_METADATA_DATE_FORMAT = "yyyyMMddHHmmss";
	
	public static final String RESOURE_ATTACH_CODE = "Attach";
	
	public static final String RESOURE_IMAGE_CODE = "Image";
	
}