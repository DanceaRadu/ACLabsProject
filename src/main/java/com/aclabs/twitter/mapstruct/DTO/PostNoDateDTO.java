package com.aclabs.twitter.mapstruct.DTO;

import com.aclabs.twitter.model.User;
import lombok.Data;

import java.util.UUID;

public @Data class PostNoDateDTO {
    private UUID id;
    private User poster;
    private String message;
}
