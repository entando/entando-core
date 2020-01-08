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
package com.agiletec.aps.system;

/**
 * Interfaccia con le principali costanti di sistema.
 *
 * @author M.Diana - E.Santoboni
 */
public interface SystemConstants {

    /**
     * Nome della property che definisce la versione di configurazione da
     * utilizzare (tipo: String)
     */
    public static final String INIT_PROP_CONFIG_VERSION = "configVersion";

    /**
     * Nome del parametro di configurazione che rappresenta l'URL esterno della
     * web application.
     */
    public static final String PAR_APPL_BASE_URL = "applicationBaseURL";

    /**
     * Nome del parametro di configurazione che rappresenta l'URL base per le
     * risorse su file
     */
    public static final String PAR_RESOURCES_ROOT_URL = "resourceRootURL";

    /**
     * Nome del parametro di configurazione che rappresenta il percorso base su
     * disco per le risorse su file
     */
    public static final String PAR_RESOURCES_DISK_ROOT = "resourceDiskRootFolder";

    public static final String PAR_FILEUPLOAD_MAXSIZE = "fileUploadMaxSize";

    /**
     * Nome parametro extra per requestContext: lingua corrente
     */
    public static final String EXTRAPAR_CURRENT_LANG = "currentLang";

    /**
     * Nome parametro extra per requestContext: pagina corrente
     */
    public static final String EXTRAPAR_CURRENT_PAGE = "currentPage";

    /**
     * Nome parametro extra per requestContext: widget corrente
     */
    public static final String EXTRAPAR_CURRENT_WIDGET = "currentShowlet";

    /**
     * Nome parametro extra per requestContext: showlet corrente
     *
     * @deprecated Use {@link #EXTRAPAR_CURRENT_WIDGET} instead
     */
    public static final String EXTRAPAR_CURRENT_SHOWLET = EXTRAPAR_CURRENT_WIDGET;

    /**
     * Nome parametro extra per requestContext: frame corrente
     */
    public static final String EXTRAPAR_CURRENT_FRAME = "currentFrame";

    /**
     * Nome parametro extra per requestContext: titoli extra pagina corrente
     */
    public static final String EXTRAPAR_EXTRA_PAGE_TITLES = "extraPageTitles";

    /**
     * Nome parametro extra per requestContext: external redirect
     */
    public static final String EXTRAPAR_EXTERNAL_REDIRECT = "externalRedirect";

    /**
     * Nome parametro extra per requestContext: Head Info Container
     */
    public static final String EXTRAPAR_HEAD_INFO_CONTAINER = "HeadInfoContainer";

    public static final String EXTRAPAR_EXECUTOR_BEAN_CONTAINER = "reqCtx_param_ExecutorBeanContainer";

    /**
     * Nome parametro di sessione: utente corrente
     */
    public static final String SESSIONPARAM_CURRENT_USER = "currentUser";

    /**
     * Nome del parametro di query string per l'identificatore di contenuto.
     */
    public static final String K_CONTENT_ID_PARAM = "contentId";

    /**
     * Nome del parametro di query string per l'identificatore di dataobject.
     */
    public static final String K_DATAOBJECT_ID_PARAM = "dataId";

    /**
     * Nome del servizio che gestisce la configurazione del sistema.
     */
    public static final String BASE_CONFIG_MANAGER = "BaseConfigManager";

    /**
     * Nome del servizio che gestisce le lingue configurate nel sistema.
     */
    public static final String LANGUAGE_MANAGER = "LangManager";

    /**
     * Nome del servizio che gestisce i tipi di showlet.
     */
    public static final String WIDGET_TYPE_MANAGER = "WidgetTypeManager";

    public static final String GUI_FRAGMENT_MANAGER = "GuiFragmentManager";

    /**
     * Nome del servizio che gestisce i tipi di showlet.
     *
     * @deprecated Use {@link #WIDGET_TYPE_MANAGER} instead
     */
    public static final String SHOWLET_TYPE_MANAGER = WIDGET_TYPE_MANAGER;

    /**
     * Nome del servizio che gestisce i modelli di pagina.
     */
    public static final String PAGE_MODEL_MANAGER = "PageModelManager";

    /**
     * Nome del servizio che gestisce le pagine del portale.
     */
    public static final String PAGE_MANAGER = "PageManager";
    public static final String PAGETOKEN_MANAGER = "PageTokenManager";

    public static final String BULK_COMMAND_MANAGER = "BulkCommandManager";

    public static final String NAVIGATOR_PARSER = "NavigatorParser";

    /**
     * Nome del servizio di gestione dei gruppi.
     */
    public static final String GROUP_MANAGER = "GroupManager";

    /**
     * Nome del servizio di gestione dei ruoli.
     */
    public static final String ROLE_MANAGER = "RoleManager";

    /**
     * Nome del del servizio di gestione degli utenti.
     */
    public static final String USER_MANAGER = "UserManager";

    /**
     * Bean Name of UserProfile Manager
     */
    public static final String USER_PROFILE_MANAGER = "UserProfileManager";

    /**
     * Nome del servizio di gestione degli URL.
     */
    public static final String URL_MANAGER = "URLManager";

    /**
     * Nome del servizio di gestione dell' i18n (localizzazione).
     */
    public static final String I18N_MANAGER = "I18nManager";

    /**
     * Nome del servizio che genera chiavi univoche (usate come id nelle
     * tabelle) ad uso degli altri servizi.
     */
    public static final String KEY_GENERATOR_MANAGER = "KeyGeneratorManager";

    /**
     * Nome del servizio di gestione delle categorie.
     */
    public static final String CATEGORY_MANAGER = "CategoryManager";

    /**
     * Nome del servizio controller.
     */
    public static final String CONTROLLER_MANAGER = "ControllerManager";

    /**
     * Name of the LicenseKey Manager.
     */
    public static final String LICENSE_KEY_MANAGER = "LicenseKeyManager";

    /**
     * Name of the Storage Manager.
     */
    public static final String STORAGE_MANAGER = "StorageManager";

    /**
     * Nome del servizio gestore cache.
     *
     * @deprecated
     */
    public static final String CACHE_MANAGER = "CacheManager";

    public static final String CACHE_INFO_MANAGER = "CacheInfoManager";

    public static final String ACTION_LOGGER_MANAGER = "ActionLogManager";

    public static final String AUTHENTICATION_PROVIDER_MANAGER = "AuthenticationProviderManager";

    public static final String AUTHORIZATION_SERVICE = "AuthorizationManager";

    public static final String API_RESPONSE_BUILDER = "ApiResponseBuilder";

    public static final String API_CATALOG_MANAGER = "ApiCatalogManager";

    public static final String API_LANG_CODE_PARAMETER = "apiMethod:langCode";

    public static final String API_USER_PARAMETER = "apiMethod:user";

    public static final String API_REQUEST_PARAMETER = "apiMethod:request";

    public static final String API_OAUTH_CONSUMER_PARAMETER = "apiMethod:OAuthConsumer";

    public static final String API_APPLICATION_BASE_URL_PARAMETER = "apiMethod:applicationBaseURL";

    public static final String API_PRODUCES_MEDIA_TYPE_PARAMETER = "apiMethod:producesMediaType";

    public static final String[] API_RESERVED_PARAMETERS = {API_LANG_CODE_PARAMETER, API_USER_PARAMETER,
        API_OAUTH_CONSUMER_PARAMETER, API_APPLICATION_BASE_URL_PARAMETER, API_PRODUCES_MEDIA_TYPE_PARAMETER};

    public static final String OAUTH_TOKEN_MANAGER = "OAuth2TokenManager";

    public static final String OAUTH_CONSUMER_MANAGER = "OAuthConsumerManager";

    public static final String DATA_OBJECT_MANAGER = "DataObjectManager";

    public static final String DATA_OBJECT_MODEL_MANAGER = "DataObjectModelManager";

    public static final String DATA_OBJECT_RENDERER_MANAGER = "BaseDataObjectRenderer";

    public static final String DATA_OBJECT_PAGE_MAPPER_MANAGER = "DataObjectPageMapperManager";

    public static final String DATA_OBJECT_DISPENSER_MANAGER = "DataObjectDispenserManager";

    public static final String DATA_OBJECT_ENGINE_MANAGER = "DataObjectSearchEngineManager";

    /**
     * Prefisso del nome del gruppo di oggetti in cache a servizio di una
     * pagina. Il nome và completato con il codice della pagina specifica.
     */
    public static final String PAGES_CACHE_GROUP_PREFIX = "PageCacheGroup_";

    /**
     * Formattazione di tutte le stringhe Date da utilizzare nel sistema.
     */
    public static final String SYSTEM_DATE_FORMAT = "yyyyMMdd";

    public static final String SYSTEM_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String CONFIG_ITEM_LANGS = "langs";

    public static final String CONFIG_ITEM_LICENSE = "license";

    public static final String CONFIG_ITEM_PARAMS = "params";

    /**
     * Parametro di sistema: abilitazione del modulo Privacy. Possibili
     * immissioni "true" o "false" (default).
     */
    public static final String CONFIG_PARAM_PM_ENABLED = "extendedPrivacyModuleEnabled";

    /**
     * Parametro di sistema a uso del modulo Privacy. Numero massimo di mesi
     * consentiti dal ultimo accesso. Nel caso che il modulo privacy sia attivo
     * e che una utenza abbia oltrepassato la soglia massima di inattività
     * dell'utenza definita da questo parametro, l'utenza sarà dichiarata
     * scaduta e in occasione del login tutte le autorizzazioni verranno
     * disabilitate.
     */
    public static final String CONFIG_PARAM_PM_MM_LAST_ACCESS = "maxMonthsSinceLastAccess";

    /**
     * Parametro di sistema a uso del modulo Privacy. Numero massimo di mesi
     * consentiti dal ultimo cambio password. Nel caso che il modulo privacy sia
     * attivo e che una utenza presenti la password invariata per un tempo oltre
     * la soglia massima definita da questo parametro, in occasione del login
     * tutte le autorizzazioni verranno disabilitate.
     */
    public static final String CONFIG_PARAM_PM_MM_LAST_PASSWORD_CHANGE = "maxMonthsSinceLastPasswordChange";

    /**
     * Parametro di sistema per la definizione dello stile della url dei link
     * generati. Se settato a 'standard', la url generata avrà la forma
     * "applicationBaseUrl/langCode/pageCode.page". Se settato a 'breadcrumbs',
     * la url generata avrà la forma
     * "applicationBaseUrl/pages/langCode/pagePath/" dove pagePath è la
     * concatenazione dei codici pagina dalla pagina radice alla pagina oggetto
     * del link.
     */
    public static final String CONFIG_PARAM_URL_STYLE = "urlStyle";

    public static final String CONFIG_PARAM_TREE_STYLE_PAGE = "treeStyle_page";
    public static final String CONFIG_PARAM_TREE_STYLE_CATEGORY = "treeStyle_category";

    public static final String CONFIG_PARAM_USE_JSESSIONID = "useJsessionId";

    public static final String CONFIG_PARAM_BASE_URL = "baseUrl";

    public static final String CONFIG_PARAM_BASE_URL_RELATIVE = "relative";
    public static final String CONFIG_PARAM_BASE_URL_FROM_REQUEST = "request";
    public static final String CONFIG_PARAM_BASE_URL_STATIC = "static";

    public static final String CONFIG_PARAM_BASE_URL_CONTEXT = "baseUrlContext";

    public static final String TREE_STYLE_CLASSIC = "classic";
    public static final String TREE_STYLE_REQUEST = "request";
    public static final String TREE_STYLE_LEVEL = "level";

    public static final String CONFIG_PARAM_URL_STYLE_CLASSIC = "classic";
    public static final String CONFIG_PARAM_URL_STYLE_BREADCRUMBS = "breadcrumbs";

    public static final String CONFIG_PARAM_START_LANG_FROM_BROWSER = "startLangFromBrowser";

    public static final String CONFIG_PARAM_HOMEPAGE_PAGE_CODE = "homePageCode";

    public static final String CONFIG_PARAM_HYPERTEXT_EDITOR_CODE = "hypertextEditor";

    public static final String CONFIG_PARAM_NOT_FOUND_PAGE_CODE = "notFoundPageCode";

    public static final String CONFIG_PARAM_ERROR_PAGE_CODE = "errorPageCode";

    public static final String CONFIG_PARAM_LOGIN_PAGE_CODE = "loginPageCode";

    public static final String CONFIG_PARAM_GRAVATAR_INTEGRATION_ENABLED = "gravatarIntegrationEnabled";

    public static final String CONFIG_PARAM_EDIT_EMPTY_FRAGMENT_ENABLED = "editEmptyFragmentEnabled";

    public static final String CONFIG_PARAM_GROUPS_ON_DEMAND = "groupsOnDemand";

    public static final String CONFIG_PARAM_CATEGORIES_ON_DEMAND = "categoriesOnDemand";

    public static final String CONFIG_PARAM_CONTENT_TYPES_ON_DEMAND = "contentTypesOnDemand";

    public static final String CONFIG_PARAM_CONTENT_MODELS_ON_DEMAND = "contentModelsOnDemand";

    public static final String CONFIG_PARAM_APIS_ON_DEMAND = "apisOnDemand";

    public static final String CONFIG_PARAM_RESOURCE_ARCHIVES_ON_DEMAND = "resourceArchivesOnDemand";

    /**
     * Lo username dell'utente amministratore, utente di default con diritti
     * massimi nel sistema.
     */
    public static final String ADMIN_USER_NAME = "admin";

    /**
     * Lo username dell'utente guest, utente di default con diritti di accesso
     * minimi ad elementi del sistema.
     */
    public static final String GUEST_USER_NAME = "guest";

    public static final String LOGIN_USERNAME_PARAM_NAME = "username";
    public static final String LOGIN_PASSWORD_PARAM_NAME = "password";

    /**
     * Code of default type of UserProfile Object
     */
    public static final String DEFAULT_PROFILE_TYPE_CODE = "PFL";

    /**
     * The name of the role for attribute attribute that contains the full name
     */
    public static final String USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME = "userprofile:fullname";

    /**
     * The name of the role for attribute attribute that contains the mail
     * address
     */
    public static final String USER_PROFILE_ATTRIBUTE_ROLE_MAIL = "userprofile:email";

    /**
     * The name of the role for attribute attribute that contains the first name
     */
    public static final String USER_PROFILE_ATTRIBUTE_ROLE_FIRST_NAME = "userprofile:firstname";

    /**
     * The name of the role for attribute attribute that contains the surname
     */
    public static final String USER_PROFILE_ATTRIBUTE_ROLE_SURNAME = "userprofile:surname";

    public static final String USER_PROFILE_ATTRIBUTE_DISABLING_CODE_ON_EDIT = "userprofile:onEdit";

    public static final String ENTANDO_THREAD_NAME_PREFIX = "EntandoThread_";

    public static final String DATA_TYPE_METADATA_DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String DATA_TYPE_ATTRIBUTE_ROLE_TITLE = "dataObject:title";

    public static final String CONFIG_ITEM_DATA_OBJECT_INDEX_SUB_DIR = "dataobjectsubdir";

    public static final String API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String PERMISSION_EDIT_DATAOBJECTS = "editDataObjects";

    public static final Integer OAUTH2_ACCESS_TOKEN_DEFAULT_VALIDITY_SECOND = 3600;

    public static final Integer OAUTH2_REFRESH_TOKEN_DEFAULT_VALIDITY_SECOND = 86400;

}
