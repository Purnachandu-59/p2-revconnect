package org.example.fullstackrevconnect.modules.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest
{
    private String content;
    private String imageUrl;
}