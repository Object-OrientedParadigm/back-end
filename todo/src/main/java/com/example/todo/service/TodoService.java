package com.example.todo.service;
import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public List<TodoEntity> create(final TodoEntity entity){
        //Validations
        validate(entity);
        repository.save(entity);
//        return repository.findById(entity.getId());
        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity){
        //Validations
        validate(entity);
        if(repository.existsById(entity.getId())) {
            repository.save(entity);
        }else throw new RuntimeException("Unknown id");

//        return repository.findById(entity.getId());
        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity>updateTodo(final TodoEntity entity){
        //Validations
        validate(entity);

        //테이블에서 id에 해당하는 데이터 셋을 가져옴
        final Optional<TodoEntity> original=repository.findById(entity.getId());

        //orignal에 담겨진 내용을 todo에 할당하고 title, done값을 변경
        original.ifPresent(todo->{
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });

        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        if(repository.existsById(entity.getId()))
            repository.deleteById(entity.getId());
        else
            throw new RuntimeException("id does not exist");

        return repository.findByUserId(entity.getUserId());
    }

    public void deleteAllByUserId(final String userId) {
        List<TodoEntity> userTodos = repository.findByUserId(userId);
        repository.deleteAll(userTodos);
    }


    public void validate(final TodoEntity entity){
        if(entity==null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUserId()==null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
