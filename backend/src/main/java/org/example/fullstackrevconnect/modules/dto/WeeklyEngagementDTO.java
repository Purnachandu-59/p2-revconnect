package org.example.fullstackrevconnect.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyEngagementDTO {

    private String day;
    private Long likes;
    private Long comments;
    private Long shares;
}