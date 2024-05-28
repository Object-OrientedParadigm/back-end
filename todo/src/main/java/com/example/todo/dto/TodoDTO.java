package com.example.todo.dto;

import com.example.todo.model.TodoEntity;
import jakarta.annotation.sql.DataSourceDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {

    private String id;
    private String title;
    private boolean done;
    private String importance;

    public TodoDTO(final TodoEntity entity){
        this.id=entity.getId();
        this.title=entity.getTitle();
        this.done=entity.isDone();
        this.importance=entity.getImportance();
    }

    public static TodoEntity toEntity(final TodoDTO dto){
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .importance(dto.getImportance())
                .build();
    }
}
