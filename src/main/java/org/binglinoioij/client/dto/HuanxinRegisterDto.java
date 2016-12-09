package org.binglinoioij.client.dto;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/30
 *
 * @author binglin
 */
public class HuanxinRegisterDto {

    private String username;

    private String password;

    public HuanxinRegisterDto() {
    }

    public HuanxinRegisterDto(String username, String password) {
        this.username = username;
        this.password = password;
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
}
