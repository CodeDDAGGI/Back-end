package com.finalProject.Back.dto.request;

import com.finalProject.Back.entity.board.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

public class ReqBoardDto {

    @Data
    @Builder
    public static class WriteBoardDto {
        @NotBlank(message = "제목을 입력하세요.")
        private String title;
        @NotBlank(message = "게시글 내용을 입력하세요.")
        private String content;
        private String category;

        public Board toEntity(Long userId) {
            return Board.builder()
                    .writerId(userId)
                    .title(title)
                    .content(content)
                    .category(category)
                    .build();
        }
    }

    @Data
    @Builder
    public static class ModifyBoardDto {
        private Long boardId;
        @NotBlank(message = "제목을 입력하세요.")
        private String title;
        @NotBlank(message = "게시글 내용을 입력하세요.")
        private String content;

        public Board toEntity() {
            return Board.builder()
                    .id(boardId)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Data
    public static class BoardListDto {
        private Long page;
        private Long limit;
        private String searchFilter;
        private String searchValue;
        private String category;
    }

}
