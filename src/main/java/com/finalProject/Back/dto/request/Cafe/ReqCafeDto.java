package com.finalProject.Back.dto.request.Cafe;

import com.finalProject.Back.entity.Cafe.Cafe;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqCafeDto {
    private Long ownerId;
    @NotBlank(message = "카페 이름을 입력해주세요.")
    private String cafename;
    @NotBlank(message = "카페 주소를 입력해주세요.")
    private String address;
    @NotBlank
    private String category;
    @NotBlank
    private String lat;
    @NotBlank
    private String lng;

    public Cafe toEntity() {
        return Cafe.builder()
                .owner_id(ownerId)
                .cafeName(cafename)
                .category(category)
                .address(address)
                .lat(lat)
                .lng(lng)
                .build();
    }
}
