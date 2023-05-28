package com.aclabs.twitter.mapstruct.DTO;

import lombok.Data;

import java.util.UUID;

public @Data class RepostDTO {
    private UUID reposterID;
    private UUID postID;
}
