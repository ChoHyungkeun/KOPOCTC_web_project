package com.example.KOPOCTC_web_project.api;

import com.example.KOPOCTC_web_project.dto.CommentDto;
import com.example.KOPOCTC_web_project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {

    @Autowired
    private CommentService commentService;

    //r - 조회 Get. "/articles/articleId/comments"
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {
        //서비스에게 일을 시키고
        List<CommentDto> dtos = commentService.comments(articleId);

        //응답
        return ResponseEntity.status(HttpStatus.OK).body(dtos);

    }


    //c - 생성
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long articleId,
            @RequestBody CommentDto commentDto) {
        CommentDto dto = commentService.createComment(articleId, commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }



    //u - 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        CommentDto dto = commentService.updateComment(id, commentDto);
        //응답
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    //d - 삭제
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long id) {
        //서비스에게 일 시키고
        CommentDto dto = commentService.deleteComment(id);

        //응답
        return ResponseEntity.status(HttpStatus.OK).body(dto);

        //

    }
}

