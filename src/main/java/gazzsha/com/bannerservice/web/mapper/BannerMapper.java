package gazzsha.com.bannerservice.web.mapper;

import gazzsha.com.bannerservice.domain.banner.Banner;
import gazzsha.com.bannerservice.web.dto.banner.BannerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface BannerMapper extends Mappable<Banner, BannerDto> {
//    @Override
//    @Mapping(target = "feature",expression = "java(entity.getFeature().getId())")
//    BannerDto toDto(Banner entity);
//
////    @Override
////    @Mappings({
////            @Mapping(target = "id", ignore = true),
////            @Mapping(target = "tags", ignore = true),
////            @Mapping(target = "feature", ignore = true)
////    })
//    Banner toEntity(BannerDto dto);
}
