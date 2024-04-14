package gazzsha.com.bannerservice.config;

import gazzsha.com.bannerservice.repository.BannerRepository;
import gazzsha.com.bannerservice.service.BannerService;
import gazzsha.com.bannerservice.service.impl.BannerServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestApplicationConfig {

    @Autowired
    private BannerRepository bannerRepository;


    @Bean
    @Primary
    private BannerService bannerService() {
        return new BannerServiceImplementation(bannerRepository);
    }
}
