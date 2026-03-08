package org.example.fullstackrevconnect.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopPostDTO {

    private String postId;
    private String content;
    private Long likes;
    private Long comments;
    private Long shares;
    private Long reach;
}