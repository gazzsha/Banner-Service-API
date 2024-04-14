package gazzsha.com.bannerservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AuthService {
    boolean hasAdminRoel(final UUID token);

    void hasAnyRole(final UUID token);
}
