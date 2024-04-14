package gazzsha.com.bannerservice.domain.exception;

public class BannerAlreadyExistWithParams extends RuntimeException {
    public BannerAlreadyExistWithParams(String message) {
        super(message);
    }
}
