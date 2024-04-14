package gazzsha.com.bannerservice.confuguration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "db.redis")
public class RedisProperty {
    private int port;
    private String host;
    private String password;
}
