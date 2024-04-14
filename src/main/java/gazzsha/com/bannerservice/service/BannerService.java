package gazzsha.com.bannerservice.service;

import gazzsha.com.bannerservice.domain.banner.Banner;

import java.util.List;

public interface BannerService {
    Banner createBanner(Banner banner, final List<Long> tags, final Long feature);

    List<Banner> getBanners(Long featureId, Long tagId, Long limit, Long offset);

    void updateBanner(Long idBanner, Banner banner) throws IllegalAccessException;

    void deleteBanner(Long bannerId);

    Banner getBannerByTagFeatureVersion(Long tagId, Long featureId, Boolean useLastVersion, boolean tokenAdmin);
}
