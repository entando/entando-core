/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page.model;

/**
 *
 * @author paddeo
 */
public class TitleDto {

    private String lang;
    private String title;

    public TitleDto() {
    }

    public TitleDto(String lang, String title) {
        this.lang = lang;
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
