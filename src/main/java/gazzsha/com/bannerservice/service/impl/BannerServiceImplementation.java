package gazzsha.com.bannerservice.service.impl;

import gazzsha.com.bannerservice.domain.banner.Banner;
import gazzsha.com.bannerservice.domain.exception.BannerAlreadyExistWithParams;
import gazzsha.com.bannerservice.domain.exception.NotFoundException;
import gazzsha.com.bannerservice.repository.BannerRepository;
import gazzsha.com.bannerservice.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BannerServiceImplementation implements BannerService {

    private final BannerRepository bannerRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public Banner createBanner(Banner banner, final List<Long> tags, final Long feature) {
        if (!bannerRepository.findBannerByTagsAndFeature(tags, feature).isEmpty()) {
            throw new BannerAlreadyExistWithParams(String.format("Banner already exist with feature %s and tag any of %s", feature, tags.toString()));
        }
        LocalDateTime now = LocalDateTime.now();
        banner.setCreatedAt(now);
        banner.setUpdatedAt(now);
        bannerRepository.save(banner);
        return banner;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Banner> getBanners(final Long featureId, final Long tagId, final Long limit, final Long offset) {
        return bannerRepository.findAllByFeatureAndTagLimitAndOffset(featureId, tagId, limit, offset);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public void updateBanner(final Long idBanner, final Banner banner) throws IllegalAccessException {
        Banner bannerInDatabase = bannerRepository.findById(idBanner).orElseThrow(() -> new NotFoundException(String.format("Banner with id %s not exist", idBanner)));
        Field[] fields = FieldUtils.getAllFields(Banner.class);
        for (Field field : fields) {
            field.setAccessible(true);
            Object object = field.get(banner);
            if (Objects.nonNull(object)) {
                ReflectionUtils.setField(field, bannerInDatabase, object);
            }
        }
        bannerInDatabase.setUpdatedAt(LocalDateTime.now());
        if (bannerRepository.findBannerByTagsAndFeature(bannerInDatabase.getTags(), bannerInDatabase.getFeature()).size() != 1)
            throw new BannerAlreadyExistWithParams(String.format("Banner already exist with feature %s and tag any of %s", bannerInDatabase.getFeature(), bannerInDatabase.getTags().toString()));
        bannerRepository.save(bannerInDatabase);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteBanner(Long bannerId) {
        Banner bannerInDatabase = bannerRepository.findById(bannerId).orElseThrow(() -> new NotFoundException(String.format("Banner with id %s not exist", bannerId)));
        bannerRepository.delete(bannerInDatabase);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "BannerService::getBannerByTagFeatureVersion", key = "#tagId + '.' + #featureId")
    public Banner getBannerByTagFeatureVersion(final Long tagId, final Long featureId, Boolean useLastVersion, final boolean tokenAdmin) {
        Banner banner = bannerRepository.findBannerByTagAndFeature(featureId, tagId).orElseThrow(() -> new NotFoundException(String.format("Banner with feature %s ans tag %s not found.", featureId, tagId)));
        if (banner.getIsActive() || tokenAdmin) return banner;
        return null;
    }
}
