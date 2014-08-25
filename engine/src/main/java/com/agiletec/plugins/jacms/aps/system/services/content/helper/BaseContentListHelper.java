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
package com.agiletec.plugins.jacms.aps.system.services.content.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author E.Santoboni
 */
public class BaseContentListHelper implements IContentListHelper {
  
	private static final Logger _logger = LoggerFactory.getLogger(BaseContentListHelper.class);
	
	@Override
    public EntitySearchFilter[] getFilters(String contentType, String filtersShowletParam, String langCode) {
        Content contentPrototype = this.getContentManager().createContentType(contentType);
        if (null == filtersShowletParam || filtersShowletParam.trim().length() == 0 || null == contentPrototype) {
            return null;
        }
        BaseFilterUtils dom = new BaseFilterUtils();
        return dom.getFilters(contentPrototype, filtersShowletParam, langCode);
    }
    
    /**
     * @deprecated From Entando 2.0 version 2.4.1. Use getFilter(String contentType, IEntityFilterBean, String) method
     */
	@Override
    public EntitySearchFilter getFilter(String contentType, IContentListFilterBean bean, String langCode) {
        return this.getFilter(contentType, (IEntityFilterBean) bean, langCode);
    }
    
	@Override
    public EntitySearchFilter getFilter(String contentType, IEntityFilterBean bean, String langCode) {
        BaseFilterUtils dom = new BaseFilterUtils();
        Content contentPrototype = this.getContentManager().createContentType(contentType);
        if (null == contentPrototype) {
            return null;
        }
        return dom.getFilter(contentPrototype, bean, langCode);
    }
    
	@Override
    public String getFilterParam(EntitySearchFilter[] filters) {
        BaseFilterUtils dom = new BaseFilterUtils();
        return dom.getFilterParam(filters);
    }
    
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, 
			key = "T(com.agiletec.plugins.jacms.aps.system.services.content.helper.BaseContentListHelper).buildCacheKey(#bean, #user)", condition = "#bean.cacheable")
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, 
			key = "T(com.agiletec.plugins.jacms.aps.system.services.content.helper.BaseContentListHelper).buildCacheKey(#bean, #user)", 
			beforeInvocation = true, 
			condition = "T(org.entando.entando.aps.system.services.cache.CacheInfoManager).isExpired(T(com.agiletec.plugins.jacms.aps.system.services.content.helper.BaseContentListHelper).buildCacheKey(#bean, #user))")
	@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENTS_ID_CACHE_GROUP_PREFIX.concat(#bean.contentType)", expiresInMinute = 30)
    public List<String> getContentsId(IContentListBean bean, UserDetails user) throws Throwable {
		List<String> contentsId = null;
        try {
            if (null == bean.getContentType()) {
                throw new ApsSystemException("Content type not defined");
            }
            Collection<String> userGroupCodes = getAllowedGroupCodes(user); //this.getAllowedGroups(user);
            contentsId = this.getContentManager().loadPublicContentsId(bean.getContentType(), bean.getCategories(), bean.getFilters(), userGroupCodes);
        } catch (Throwable t) {
        	_logger.error("Error extracting contents id", t);
            //ApsSystemUtils.logThrowable(t, this, "extractContentsId");
            throw new ApsSystemException("Error extracting contents id", t);
        }
        return contentsId;
    }
	
    /**
     * Return the groups to witch execute the filter to contents.
     * The User object is non null, extract the groups from the user, else 
     * return a collection with only the "free" group. 
     * @param user The user. Can be null.
     * @return The groups to witch execute the filter to contents.
	 * @deprecated 
     */
    protected Collection<String> getAllowedGroups(UserDetails user) {
        return getAllowedGroupCodes(user);
    }
	
    public static Collection<String> getAllowedGroupCodes(UserDetails user) {
        Set<String> allowedGroup = new HashSet<String>();
		allowedGroup.add(Group.FREE_GROUP_NAME);
        if (null != user && null != user.getAuthorities()) {
			for (int i = 0; i < user.getAuthorities().length; i++) {
				IApsAuthority authority = user.getAuthorities()[i];
				if (authority instanceof Group) {
					allowedGroup.add(authority.getAuthority());
				}
			}
        }
        return allowedGroup;
    }
	
	public static String buildCacheKey(IContentListBean bean, UserDetails user) {
		Collection<String> userGroupCodes = getAllowedGroupCodes(user);
		return buildCacheKey(bean, userGroupCodes);
	}
	
    protected static String buildCacheKey(IContentListBean bean, Collection<String> userGroupCodes) {
        StringBuilder cacheKey = new StringBuilder();
        if (null != bean.getListName()) {
            cacheKey.append("LISTNAME_").append(bean.getListName());
        }
        if (null != bean.getContentType()) {
            cacheKey.append("TYPE_").append(bean.getContentType());
        }
        List<String> groupCodes = new ArrayList<String>(userGroupCodes);
        if (!groupCodes.contains(Group.FREE_GROUP_NAME)) {
            groupCodes.add(Group.FREE_GROUP_NAME);
        }
        Collections.sort(groupCodes);
        for (int i = 0; i < groupCodes.size(); i++) {
            if (i == 0) {
                cacheKey.append("-GROUPS");
            }
            String code = groupCodes.get(i);
            cacheKey.append("_").append(code);
        }
        if (null != bean.getCategories()) {
            List<String> categoryCodes = Arrays.asList(bean.getCategories());
            Collections.sort(categoryCodes);
            for (int j = 0; j < categoryCodes.size(); j++) {
                if (j == 0) {
                    cacheKey.append("-CATEGORIES");
                }
                String code = categoryCodes.get(j);
                cacheKey.append("_").append(code);
            }
        }
        if (null != bean.getFilters()) {
            for (int k = 0; k < bean.getFilters().length; k++) {
                if (k == 0) {
                    cacheKey.append("-FILTERS");
                }
                EntitySearchFilter filter = bean.getFilters()[k];
                cacheKey.append("_").append(filter.toString());
            }
        }
        return cacheKey.toString();
    }

    public static String concatStrings(Collection<String> values, String separator) {
        StringBuilder concatedValues = new StringBuilder();
        if (null == values) {
            return concatedValues.toString();
        }
        boolean first = true;
        Iterator<String> valuesIter = values.iterator();
        while (valuesIter.hasNext()) {
            if (!first) {
                concatedValues.append(separator);
            }
            concatedValues.append(valuesIter.next());
            first = false;
        }
        return concatedValues.toString();
    }

    public static List<String> splitValues(String concatedValues, String separator) {
        List<String> values = new ArrayList<String>();
        if (concatedValues != null && concatedValues.trim().length() > 0) {
            String[] codes = concatedValues.split(separator);
            for (int i = 0; i < codes.length; i++) {
                values.add(codes[i]);
            }
        }
        return values;
    }
	/*
    protected ICacheManager getCacheManager() {
        return _cacheManager;
    }
    public void setCacheManager(ICacheManager cacheManager) {
        this._cacheManager = cacheManager;
    }
	*/
    protected IContentManager getContentManager() {
        return _contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }
	/*
    protected IAuthorizationManager getAuthorizationManager() {
        return _authorizationManager;
    }
    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this._authorizationManager = authorizationManager;
    }
    */
    //private ICacheManager _cacheManager;
    private IContentManager _contentManager;
    //private IAuthorizationManager _authorizationManager;
    
}
