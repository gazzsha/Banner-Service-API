package gazzsha.com.bannerservice.domain.banner;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "banner", schema = "banners")
@EqualsAndHashCode
public class Banner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "tag_id")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "banners_tags", schema = "banners", joinColumns = @JoinColumn(name = "banner_id"))
    @Cascade(CascadeType.ALL)
    private List<Long> tags;

    @Column(name = "feature_id")
    private Long feature;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    @EqualsAndHashCode.Exclude
    private Map<String, Object> content;

    @Column(name = "is_active")
    @EqualsAndHashCode.Exclude
    private Boolean isActive;


    @Column(name = "created_at")
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedAt;
}
