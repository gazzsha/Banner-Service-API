package gazzsha.com.bannerservice.domain.feature;


import gazzsha.com.bannerservice.domain.banner.Banner;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

//@Entity
//@Data
//public class Feature {
//    @Id
//    @EqualsAndHashCode.Exclude
//    private Long id;
//
////    @OneToMany
////    @EqualsAndHashCode.Exclude
////    private List<Banner> banner;
//
//}
