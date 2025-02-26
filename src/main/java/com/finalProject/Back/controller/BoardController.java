package com.finalProject.Back.controller;

import com.finalProject.Back.aspect.annotation.ValidAop;
import com.finalProject.Back.dto.request.ReqBoardDto;
import com.finalProject.Back.security.principal.PrincipalUser;
import com.finalProject.Back.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @ValidAop
    @PostMapping("/board")
    public ResponseEntity<?> write(@Valid @RequestBody ReqBoardDto.WriteBoardDto dto, BindingResult bindingResult) {
        return ResponseEntity.ok().body(boardService.write(dto));
    }

    @GetMapping("/board/list")
    public ResponseEntity<?> getList(ReqBoardDto.BoardListDto dto) {
        return ResponseEntity.ok().body(boardService.getList(dto));
    }

    @GetMapping("/board/notice")
    public ResponseEntity<?> getCafeNoticeList() {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok().body(boardService.getNoticeList(principalUser.getId()));
    }
    @GetMapping("/board/{boardId}/content")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(boardService.getContent(boardId));
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getDetail(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(boardService.getDetail(boardId));
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.ok().body(true);
    }

    @ValidAop
    @PutMapping("/board/{boardId}")
    public ResponseEntity<?> modify(@RequestBody ReqBoardDto.ModifyBoardDto dto, BindingResult bindingResult) {
        return ResponseEntity.ok().body(boardService.modify(dto));
    }

    @GetMapping("/board/{boardId}/like")
    public ResponseEntity<?> getLikes(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(boardService.getLike(boardId));
    }

    @PostMapping("/board/{boardId}/like")
    public ResponseEntity<?> like(@PathVariable Long boardId) {
        boardService.like(boardId);
        return ResponseEntity.ok().body(true);
    }

    @DeleteMapping("/board/like/{boardLikeId}")
    public ResponseEntity<?> disLike(@PathVariable Long boardLikeId) {
        boardService.disLike(boardLikeId);
        return ResponseEntity.ok().body(true);
    }

}
