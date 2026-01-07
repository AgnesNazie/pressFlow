package se.lexicon.pressflow.service;

import se.lexicon.pressflow.dto.TodoAssignmentDto;
import se.lexicon.pressflow.dto.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto create(TodoDto todoDto);

    TodoDto findById(Long id);

    List<TodoDto> findAll();

    TodoDto update(Long id, TodoDto todoDto);

    void delete(Long id);

    List<TodoDto> findByPersonId(Long personId);

    List<TodoDto> findByCompleted(boolean completed);

    List<TodoDto> findOverdueTodos();

    void assignTodo(Long todoId, TodoAssignmentDto dto);
}
