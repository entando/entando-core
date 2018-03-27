package com.agiletec.aps.system.services.authorization.model;

import java.util.Date;

public class UserDto {

    private Date registrationDate;
    private Date lastAccess;
    private Date lastPasswordChange;
    private boolean disabled;
    private boolean accountNotExpired;
    private boolean credentialsNotExpired;

    private int maxMonthsSinceLastAccess;
    private int maxMonthsSinceLastPasswordChange;

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    public int getMaxMonthsSinceLastAccess() {
        return maxMonthsSinceLastAccess;
    }

    public void setMaxMonthsSinceLastAccess(int maxMonthsSinceLastAccess) {
        this.maxMonthsSinceLastAccess = maxMonthsSinceLastAccess;
    }

    public int getMaxMonthsSinceLastPasswordChange() {
        return maxMonthsSinceLastPasswordChange;
    }

    public void setMaxMonthsSinceLastPasswordChange(int maxMonthsSinceLastPasswordChange) {
        this.maxMonthsSinceLastPasswordChange = maxMonthsSinceLastPasswordChange;
    }



}
