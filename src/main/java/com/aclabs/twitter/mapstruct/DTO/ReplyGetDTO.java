package com.aclabs.twitter.mapstruct.DTO;

import lombok.Data;

import java.sql.Timestamp;

public @Data class ReplyGetDTO {

    private String replyText;
    private String replierUsername;
    private Timestamp postDate;
    private boolean isPublic;
}
