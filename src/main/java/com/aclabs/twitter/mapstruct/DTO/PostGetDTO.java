package com.aclabs.twitter.mapstruct.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

public @Data class PostGetDTO {
    private String posterUsername;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bucharest")
    private Timestamp postDate;
    private List<ReplyGetDTO> replies;
    private List<LikeGetDTO> postLikes;
}
