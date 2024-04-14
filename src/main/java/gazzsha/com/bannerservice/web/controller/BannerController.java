package gazzsha.com.bannerservice.web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import gazzsha.com.bannerservice.domain.banner.Banner;
import gazzsha.com.bannerservice.service.AuthService;
import gazzsha.com.bannerservice.service.BannerService;
import gazzsha.com.bannerservice.web.dto.banner.BannerDto;
import gazzsha.com.bannerservice.web.dto.validation.OnCreate;
import gazzsha.com.bannerservice.web.dto.view.Created;
import gazzsha.com.bannerservice.web.dto.view.Exist;
import gazzsha.com.bannerservice.web.dto.view.User;
import gazzsha.com.bannerservice.web.mapper.BannerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
public class BannerController {

    private final BannerMapper bannerMapper;

    private final BannerService bannerService;

    private final AuthService authService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @JsonView(Created.class)
    @PostMapping(value = "/banner")
    public ResponseEntity<BannerDto> createBanner(@RequestHeader(value = "token", required = true) UUID token,
                                                  @RequestBody @Validated(OnCreate.class) final BannerDto dto) {
        authService.hasAdminRoel(token);
        Banner banner = bannerMapper.toEntity(dto);
        bannerService.createBanner(banner, dto.getTags(), dto.getFeature());
        return new ResponseEntity<>(bannerMapper.toDto(banner), HttpStatus.CREATED);
    }

    @JsonView(Exist.class)
    @GetMapping(value = "/banner")
    public ResponseEntity<List<BannerDto>> getBanner(@RequestHeader(value = "token", required = true) UUID token,
                                                     @RequestParam(name = "feature_id", required = false) Long featureId,
                                                     @RequestParam(name = "tag_id", required = false) Long tagId,
                                                     @RequestParam(name = "limit", required = false) Long limit,
                                                     @RequestParam(name = "offset", required = false) Long offset) {
        authService.hasAdminRoel(token);
        return ResponseEntity.ok(bannerMapper.toDto(bannerService.getBanners(featureId, tagId, limit, offset)));
    }


    @PatchMapping(value = "/banner/{id}")
    public ResponseEntity<Void> updateBanner(@RequestHeader(value = "token", required = true) UUID token,
                                             @PathVariable(name = "id") Long bannerId, @RequestBody BannerDto dto) throws IllegalAccessException {
        authService.hasAdminRoel(token);
        bannerService.updateBanner(bannerId, bannerMapper.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/banner/{id}")
    public ResponseEntity<Void> deleteBanner(@RequestHeader(value = "token", required = true) UUID token,
                                             @PathVariable(name = "id") Long bannerId) {
        authService.hasAdminRoel(token);
        bannerService.deleteBanner(bannerId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/user_banner")
    @JsonView(User.class)
    public ResponseEntity<BannerDto> getBannerForUser(@RequestHeader(value = "token", required = true) UUID token,
                                                      @RequestParam(value = "tag_id", required = true) Long tagId,
                                                      @RequestParam(value = "feature_id", required = true) Long featureId,
                                                      @RequestParam(value = "use_last_revision", required = false, defaultValue = "false") Boolean useLastVersion) {
        authService.hasAnyRole(token);
        Banner banner = bannerService.getBannerByTagFeatureVersion(tagId, featureId, useLastVersion, authService.hasAdminRoel(token));
        return ResponseEntity.ok(bannerMapper.toDto(banner));
    }
}
