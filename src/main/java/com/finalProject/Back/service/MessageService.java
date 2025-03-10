package com.finalProject.Back.service;

import com.finalProject.Back.dto.request.ReqMessageDto;
import com.finalProject.Back.entity.Message;
import com.finalProject.Back.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    public Long save(ReqMessageDto dto) {
        Message message = dto.toEntity();

        messageMapper.save(message);

        Long messageId = message.getId();

        // Print the generated ID
        System.out.println("Generated Message ID: " + messageId);

        return messageId;  // Return the generated ID
    }

    public List<Message> getMissedMessages(Long userId , Long lastId) {
        return messageMapper.findMessagesAfterId(userId,  lastId);
    }

    public Long findByTypeId(String type, Long contentId){
        if(type.equals("게시글")){
            return boardMapper.findByBoardId(contentId);
        }else if(type.equals("댓글")){
            return commentMapper.findByCommentId(contentId);
        }else if(type.equals("리뷰")){
            return reviewMapper.findById(contentId);
        }
        return null;
    }

    public List<Message> findByUserId(Long UserId) {
        return messageMapper.findByUserId(UserId);
    }

    public int deleteMessage(Long id){
        return messageMapper.deleteById(id);
    }
}
