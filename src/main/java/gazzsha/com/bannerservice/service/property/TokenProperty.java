package gazzsha.com.bannerservice.service.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
@ConfigurationProperties(prefix = "security.token")
public class TokenProperty {
    private UUID admin;
    private UUID user;
}
