package gazzsha.com.bannerservice.web.dto.banner;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import gazzsha.com.bannerservice.web.dto.validation.OnCreate;
import gazzsha.com.bannerservice.web.dto.view.Created;
import gazzsha.com.bannerservice.web.dto.view.Exist;
import gazzsha.com.bannerservice.web.dto.view.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;


@Data
public class BannerDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "banner_id")
    @JsonView(value = {Created.class, Exist.class})
    private Long id;

    @NotNull(message = "tag_ids must be not null.", groups = {OnCreate.class})
    @JsonProperty(value = "tag_ids")
    @JsonView(value = {Exist.class})
    private List<Long> tags;

    @NotNull(message = "feature_id must be not null.", groups = {OnCreate.class})
    @JsonProperty(value = "feature_id")
    @JsonView(value = {Exist.class})
    private Long feature;

    @NotNull(message = "Content must be not null", groups = {OnCreate.class})
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonView(value = {Exist.class, User.class})
    private LinkedHashMap<String, Object> content;

    @NotNull(message = "is_active mist not be null", groups = {OnCreate.class})
    @JsonProperty(value = "is_active")
    @JsonView(value = {Exist.class})
    private Boolean isActive;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "created_at")
    @JsonView(value = {Exist.class})
    private LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "updated_at")
    @JsonView(value = {Exist.class})
    private LocalDateTime updatedAt;
}
