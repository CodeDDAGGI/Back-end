package com.finalProject.Back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    private Long id;
    private Long boardId;
    private Long parentId;
    private String content;
    private Long writerId;
    private LocalDateTime writeDate;
    private int level;
    private String nickname;
    private String img;
    private String path;
    private String name;
    private String title;
}
