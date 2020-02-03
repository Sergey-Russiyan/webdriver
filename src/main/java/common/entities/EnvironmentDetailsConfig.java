package common.entities;

import lombok.Getter;

@Getter
public enum EnvironmentDetailsConfig {

    PROD    ("prod",    "https://www.bing.com/"),
    DEV     ("dev",    "https://www.dev-bing.com/"),
    TEST    ("test",    "https://www.test-bing.com/"),

    ;
    public final String dbName;
    private final String baseUrl;

    EnvironmentDetailsConfig(String dbName, String baseUrl) {
        this.dbName = dbName;
        this.baseUrl = baseUrl;
    }
    public String getBaseUrl() {
        return baseUrl;
    }

}
