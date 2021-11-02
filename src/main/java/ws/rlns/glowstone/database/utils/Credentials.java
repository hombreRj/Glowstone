package ws.rlns.glowstone.database.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Credentials {

    private String host, password, user;
    private int port;
    private String uri = null;

    public Credentials(String user, String password, String host, int port) {
        this.user = user;
        this.host = host;
        this.password = password;
        this.port = port;
    }

    public Credentials(String uri) {
        this.uri = uri;
    }
}
