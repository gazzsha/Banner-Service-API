package gazzsha.com.bannerservice.service.impl;


import gazzsha.com.bannerservice.domain.exception.ForbiddenError;
import gazzsha.com.bannerservice.domain.exception.UnauthorizedError;
import gazzsha.com.bannerservice.service.AuthService;
import gazzsha.com.bannerservice.service.property.TokenProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final TokenProperty tokenProperty;

    @Override
    public boolean hasAdminRoel(UUID token) {
        if (Objects.isNull(token) || token.toString().isEmpty()) throw new UnauthorizedError();
        if (!tokenProperty.getAdmin().equals(token)) throw new ForbiddenError();
        return true;
    }

    @Override
    public void hasAnyRole(UUID token) {
        if (Objects.isNull(token) || token.toString().isEmpty()) throw new UnauthorizedError();
        if (!tokenProperty.getAdmin().equals(token) && !tokenProperty.getUser().equals(token))
            throw new ForbiddenError();

    }
}
