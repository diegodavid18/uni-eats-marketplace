package com.remington.unieats.marketplace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "powerbi")
public class PowerBIConfig {
    
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private String authorityUrl;
    private String scope;
    private String workspaceId;
    private String reportId;
    private String username;
    private String password;
    
    // Constructors
    public PowerBIConfig() {
        this.authorityUrl = "https://login.microsoftonline.com/";
        this.scope = "https://analysis.windows.net/powerbi/api/.default";
    }
    
    // Getters and Setters
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getAuthorityUrl() {
        return authorityUrl;
    }
    
    public void setAuthorityUrl(String authorityUrl) {
        this.authorityUrl = authorityUrl;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public String getWorkspaceId() {
        return workspaceId;
    }
    
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
    
    public String getReportId() {
        return reportId;
    }
    
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAuthorityUri() {
        return authorityUrl + tenantId;
    }
}