package com.aclabs.twitter.mapstruct.mappers;

import com.aclabs.twitter.mapstruct.DTO.LikeGetDTO;
import com.aclabs.twitter.mapstruct.DTO.PostGetDTO;
import com.aclabs.twitter.mapstruct.DTO.ReplyGetDTO;
import com.aclabs.twitter.mapstruct.DTO.UserSearchDTO;
import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DTOMapper {

    @Mapping(target = "posterUsername", source = "post.poster.username")
    @Mapping(target = "message", source = "post.message")
    @Mapping(target = "postDate", source = "post.postDate")
    PostGetDTO postToPostGetDTO(Post post);

    @Mapping(target = "replierUsername", source = "reply.replier.username")
    ReplyGetDTO replyToReplyGetDTO(Reply reply);

    @Mapping(target = "likerID", source = "likeID.likerID")
    LikeGetDTO likeToLikeGetDTO(Like like);

    UserSearchDTO userToUserSearchDTO(User user);

}
