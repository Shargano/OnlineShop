package net.thumbtack.onlineshop.dto.response;

public class SettingResponse {
    private int rest_http_port;
    private int maxNameLength;
    private int minPasswordLength;

    public SettingResponse() {
    }

    public SettingResponse(int rest_http_port, int maxNameLength, int minPasswordLength) {
        this.rest_http_port = rest_http_port;
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
    }

    public int getRest_http_port() {
        return rest_http_port;
    }

    public void setRest_http_port(int rest_http_port) {
        this.rest_http_port = rest_http_port;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }
}