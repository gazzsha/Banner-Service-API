package gazzsha.com.bannerservice.service;

import gazzsha.com.bannerservice.domain.banner.Banner;
import gazzsha.com.bannerservice.domain.exception.BannerAlreadyExistWithParams;
import gazzsha.com.bannerservice.domain.exception.NotFoundException;
import gazzsha.com.bannerservice.service.impl.BannerServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BannerServiceImplTest {

    @Autowired
    private BannerServiceImplementation bannerService;

    @Test
    @Order(1)
    void createBanner() {
        Banner banner = new Banner();
        banner.setTags(List.of(1L, 2L, 3L));
        banner.setIsActive(true);
        banner.setContent(Map.of("info", "info"));
        banner.setFeature(1L);
        Banner createdBanner = bannerService.createBanner(banner, banner.getTags(), banner.getFeature());
        Assertions.assertNotNull(createdBanner.getCreatedAt());
        Assertions.assertEquals(List.of(1L, 2L, 3L), banner.getTags());
        Assertions.assertEquals(1L, banner.getFeature());
        Assertions.assertNotNull(createdBanner.getUpdatedAt());
        Assertions.assertNotNull(createdBanner.getId());
        Assertions.assertEquals(createdBanner.getId(), 1L);
        Assertions.assertEquals(createdBanner.getUpdatedAt(), createdBanner.getCreatedAt());
    }

    @Test
    @Order(2)
    void createBannerWithExistingTagsOrFeature() {
        Banner banner = new Banner();
        banner.setTags(List.of(1L, 2L, 3L));
        banner.setIsActive(true);
        banner.setContent(Map.of("info", "info"));
        banner.setFeature(1L);
        Assertions.assertThrows(BannerAlreadyExistWithParams.class,
                () -> bannerService.createBanner(banner, banner.getTags(), banner.getFeature()));
    }

    @Test
    @Order(3)
    void updateBanner() throws IllegalAccessException {
        Banner banner = new Banner();
        banner.setContent(Map.of("new content", "content"));
        bannerService.updateBanner(1L, banner);
        Banner bannerByTagFeatureVersion = bannerService.getBannerByTagFeatureVersion(1L, 1L, false, true);
        Assertions.assertNotEquals(bannerByTagFeatureVersion.getUpdatedAt(), bannerByTagFeatureVersion.getCreatedAt());
        Assertions.assertEquals(bannerByTagFeatureVersion.getContent(), Map.of("new content", "content"));
    }

    @Test
    @Order(4)
    void updateBannerWithIdenticalFeatureAndTags() throws IllegalAccessException {
        Banner banner = new Banner();
        banner.setTags(List.of(4L, 5L, 6L));
        banner.setIsActive(true);
        banner.setContent(Map.of("info", "info"));
        banner.setFeature(2L);
        Banner createdBanner = bannerService.createBanner(banner, banner.getTags(), banner.getFeature());
        Assertions.assertNotNull(createdBanner.getCreatedAt());
        Assertions.assertEquals(List.of(4L, 5L, 6L), banner.getTags());
        Assertions.assertEquals(2L, banner.getFeature());
        Assertions.assertNotNull(createdBanner.getUpdatedAt());
        Assertions.assertNotNull(createdBanner.getId());
        Assertions.assertEquals(createdBanner.getUpdatedAt(), createdBanner.getCreatedAt());
        banner.setFeature(1L);
        banner.setTags(List.of(2L));
        Assertions.assertThrows(BannerAlreadyExistWithParams.class,
                () -> bannerService.updateBanner(banner.getId(), banner));
        Assertions.assertNotNull(createdBanner.getId());
    }

    @Test
    @Order(5)
    void updateNonExistingIdBanner() {
        Banner banner = new Banner();
        Long id = -1L;
        Assertions.assertThrows(NotFoundException.class,
                () -> bannerService.updateBanner(id, banner));
    }

    @Test
    void deleteNonExistingBanner() {
        Long id = -1L;
        Assertions.assertThrows(NotFoundException.class,
                () -> bannerService.deleteBanner(id));
    }

    @Test
    @Order(6)
    void deleteBanner() {
        Long id = 1L;
        Assertions.assertDoesNotThrow(() -> bannerService.deleteBanner(id));
    }

    @Test
    void getBannersByFilter() {
        Banner banner = new Banner();
        banner.setFeature(10L);
        banner.setIsActive(true);
        banner.setContent(Map.of("info", "info"));
        banner.setTags(List.of(10L, 20L, 30L));
        bannerService.createBanner(banner, banner.getTags(), banner.getFeature());
        Banner bannerSec = new Banner();
        bannerSec.setFeature(10L);
        bannerSec.setIsActive(true);
        bannerSec.setContent(Map.of("info", "info"));
        bannerSec.setTags(List.of(40L, 50L, 60L));
        bannerService.createBanner(bannerSec, bannerSec.getTags(), bannerSec.getFeature());
        List<Banner> banners = bannerService.getBanners(10L, null, 100L, 0L);
        Assertions.assertEquals(banners.size(), 2);
        for (var bannerOfFilter : banners) {
            Assertions.assertEquals(bannerOfFilter.getContent(), Map.of("info", "info"));
            Assertions.assertEquals(bannerOfFilter.getIsActive(), true);
        }
        List<Banner> nullBanners = bannerService.getBanners(10L, null, 100L, 2L);
        Assertions.assertEquals(nullBanners.size(), 0L);
        List<Banner> bannerWithFeatureAndTags = bannerService.getBanners(10L, 20L, 100L, 0L);
        Assertions.assertDoesNotThrow(() -> bannerWithFeatureAndTags.get(0));
        var firstBanner = bannerWithFeatureAndTags.get(0);
        Assertions.assertArrayEquals(firstBanner.getTags().toArray(), List.of(10L, 20L, 30L).toArray());
        Assertions.assertEquals(firstBanner.getFeature(), 10L);
        var allBanners = bannerService.getBanners(null, null, 100L, 0L);
        Assertions.assertNotNull(allBanners);
    }

    @Test
    void getBannerByTagFeature() {
        Banner banner = new Banner();
        banner.setFeature(100L);
        banner.setIsActive(false);
        banner.setTags(List.of(100L, 200L, 300L));
        bannerService.createBanner(banner, banner.getTags(), banner.getFeature());
        Banner bannerByTagFeatureVersion1 = bannerService.getBannerByTagFeatureVersion(100L, 100L, false, true);
        Banner bannerByTagFeatureVersion2 = bannerService.getBannerByTagFeatureVersion(200L, 100L, false, true);
        Banner bannerByTagFeatureVersion3 = bannerService.getBannerByTagFeatureVersion(300L, 100L, false, true);
        AssertBanners(bannerByTagFeatureVersion1, bannerByTagFeatureVersion2);
        AssertBanners(bannerByTagFeatureVersion2, bannerByTagFeatureVersion3);
        AssertBanners(bannerByTagFeatureVersion1, bannerByTagFeatureVersion3);
        Assertions.assertEquals(bannerByTagFeatureVersion1.getFeature(), 100L);
        Assertions.assertEquals(bannerByTagFeatureVersion2.getFeature(), 100L);
        Assertions.assertArrayEquals(bannerByTagFeatureVersion3.getTags().toArray(), new Long[]{100L, 200L, 300L});
        Assertions.assertArrayEquals(bannerByTagFeatureVersion2.getTags().toArray(), new Long[]{100L, 200L, 300L});
        Assertions.assertArrayEquals(bannerByTagFeatureVersion1.getTags().toArray(), new Long[]{100L, 200L, 300L});
    }

    private void AssertBanners(Banner banner1,Banner banner2) {
        Assertions.assertEquals(banner1.getId(),banner2.getId());
        Assertions.assertArrayEquals(banner1.getTags().toArray(), banner2.getTags().toArray());
        Assertions.assertEquals(banner1.getFeature(), banner2.getFeature());
        Assertions.assertEquals(banner1.getContent(), banner2.getContent());
        Assertions.assertEquals(banner1.getIsActive(), banner2.getIsActive());
        Assertions.assertEquals(banner1.getCreatedAt(), banner2.getCreatedAt());
        Assertions.assertEquals(banner1.getUpdatedAt(), banner2.getUpdatedAt());
    }

}
