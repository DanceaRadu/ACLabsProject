package com.aclabs.twitter.mapstruct.DTO;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

public @Data class PostGetDTO {
    private String posterUsername;
    private String message;
    private Timestamp postDate;
    private List<ReplyGetDTO> replies;
    private List<LikeGetDTO> postLikes;
}
