package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor.OptimalPropertyAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@CrossOrigin(origins="*")
@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?>createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
        try{
            log.info("Log:createTodo entrance");

            //dto를 이용해 테이블에 저장하기 위한 엔티티 생성
            TodoEntity entity=TodoDTO.toEntity(dto);
            log.info("Log:dto => entity ok!");

            entity.setId(null);
            entity.setUserId(userId);

            //service.create를 통해 repository에 entity를 저장한다
            List<TodoEntity> entities=service.create(entity);
            log.info("Log:service.create ok!");

            //entities를 dtos로 스트림 변환
            List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            log.info("Log:entities => dtos ok!");

            //Response DTO를 생성
            ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();
            log.info("Log: responsedto ok!");

            //HTTP Status 200 상태로 response 전송
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error=e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
        List<TodoEntity> entities=service.retrieve(userId);
        List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //HTTP Status 200 상태로 response를 전송
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/update")
    public ResponseEntity<?>update(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
        try{
            //dto를 이용해 테이블에 저장하기 위한 entity를 생성한다
            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            //service.create를 통해 repository에 entity를 저장한다
            List<TodoEntity> entities = service.update(entity);

            //entitie -> dtos로 스트림 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //response DTO 생성
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //http status 200 상태로 response를 전송
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?>updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
        try {
            //dto를 이용해 테이블에 저장하기 위한 entity를 생성
            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.updateTodo(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId){
        try{
            TodoEntity entity=TodoDTO.toEntity(dto);

            entity.setUserId(userId);
            List<TodoEntity> entities=service.delete(entity);

            List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //Response dto 생성
            ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            String error=e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllTodos(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId) {
        try {
            // item으로 넘긴 데이터를 사용
            TodoEntity entity = TodoDTO.toEntity(dto);

            // entity에 userId를 설정
            entity.setUserId(userId);
            service.deleteAllByUserId(userId);

            return ResponseEntity.ok().body("All todos deleted successfully.");
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
