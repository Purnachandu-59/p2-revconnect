package org.example.fullstackrevconnect.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowerGrowthDTO {

    private String date;
    private Long newFollowers;
}