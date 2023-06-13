package com.aclabs.twitter.mapstruct.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

public @Data class ReplyGetDTO {

    private String replyText;
    private String replierUsername;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bucharest")
    private Timestamp postDate;
    private boolean isPublic;
}
