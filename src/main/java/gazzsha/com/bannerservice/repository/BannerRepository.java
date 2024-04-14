package gazzsha.com.bannerservice.repository;
import gazzsha.com.bannerservice.domain.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {


    @Query(value = """
            SELECT (b.id) FROM banners.banner b
                              LEFT JOIN banners.banners_tags t ON b.id = t.banner_id
            WHERE b.feature_id =:featureId  AND tag_id IN (:tags)
            GROUP BY id""", nativeQuery = true)
    List<Long> findBannerByTagsAndFeature(@Param("tags") List<Long> tags, @Param("featureId") Long feature);


    @Query(value = """
            SELECT b.id,b.feature_id,b.content,b.is_active,b.updated_at,b.created_at FROM banners.banner b
            JOIN banners.banners_tags t ON b.id = t.banner_id
            WHERE (:tagId IS NULL OR t.tag_id = :tagId)
            AND (:featureId IS NULL OR b.feature_id = :featureId)
            GROUP BY b.id
            LIMIT :limitValue OFFSET :offsetValue
            """, nativeQuery = true)
    List<Banner> findAllByFeatureAndTagLimitAndOffset(@Param(value = "featureId") Long featureId,
                                                      @Param(value = "tagId") Long tagId,
                                                      @Param(value = "limitValue") Long limit,
                                                      @Param(value = "offsetValue") Long offset);


    @Query(value = """
                    SELECT * FROM banners.banner b
                    LEFT JOIN banners.banners_tags t ON b.id = t.banner_id
                    WHERE feature_id =:feature_id AND tag_id =:tag_id
            """, nativeQuery = true)
    Optional<Banner> findBannerByTagAndFeature(@Param(value = "feature_id") Long featureId,
                                               @Param(value = "tag_id") Long tagId);
}
