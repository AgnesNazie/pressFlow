package se.lexicon.pressflow.service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.lexicon.pressflow.dto.AttachmentDto;
import se.lexicon.pressflow.dto.TodoAssignmentDto;
import se.lexicon.pressflow.dto.TodoDto;
import se.lexicon.pressflow.entity.*;
import se.lexicon.pressflow.repository.PersonRepository;
import se.lexicon.pressflow.repository.TodoAssignmentRepository;
import se.lexicon.pressflow.repository.TodoRepository;
import se.lexicon.pressflow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {


    private final TodoRepository todoRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final TodoAssignmentRepository todoAssignmentRepository;

    public TodoServiceImpl(TodoRepository todoRepository, PersonRepository personRepository,UserRepository userRepository,TodoAssignmentRepository todoAssignmentRepository   ) {
        this.todoRepository = todoRepository;
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.todoAssignmentRepository= todoAssignmentRepository;
    }

    private TodoDto convertToDto(Todo todo) {
        List<AttachmentDto> attachmentDtos = todo.getAttachments().stream()
                .map(attachment -> new AttachmentDto(
                        attachment.getId(),
                        attachment.getFileName(),
                        attachment.getFileType(),
                        attachment.getData()
                ))
                .collect(Collectors.toList());

        return TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .dueDate(todo.getDueDate())
                .personId(todo.getPerson() != null ? todo.getPerson().getId() : null)
                .numberOfAttachments(todo.getAttachments().size())
                .attachments(attachmentDtos)
                .build();
    }


    private Todo convertToEntity(TodoDto todoDto) {
        Todo todo = new Todo(
                todoDto.title(),
                todoDto.description(),
                todoDto.completed(),
                todoDto.dueDate()
        );

        // NEW: Automatically assign the logged-in person instead of using todoDto.personId
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // NEW
        Person person = personRepository.findByUserUsername(username) // NEW
                .orElseThrow(() -> new RuntimeException("Person not found for user: " + username)); // NEW
        todo.setPerson(person); // NEW

        // Add attachments if present
        if (todoDto.attachments() != null && !todoDto.attachments().isEmpty()) {
            for (AttachmentDto attachmentDto : todoDto.attachments()) {
                Attachment attachment = new Attachment(
                        attachmentDto.fileName(),
                        attachmentDto.fileType(),
                        attachmentDto.data()
                );
                todo.addAttachment(attachment);
            }
        }

        return todo;
    }

    @Override
    public TodoDto create(TodoDto todoDto) {
        Todo todo = convertToEntity(todoDto);
        Todo savedTodo = todoRepository.save(todo);
        return convertToDto(savedTodo);
    }

    @Override
    public TodoDto findById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        return convertToDto(todo);
    }

    @Override
    public List<TodoDto> findAll() {
        return todoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDto update(Long id, TodoDto todoDto) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        validateOwnership(existingTodo);
        existingTodo.setTitle(todoDto.title());
        existingTodo.setDescription(todoDto.description());
        existingTodo.setCompleted(todoDto.completed());
        existingTodo.setDueDate(todoDto.dueDate());

        if (todoDto.personId() != null) {
            Person person = personRepository.findById(todoDto.personId())
                    .orElseThrow(() -> new RuntimeException("Person not found"));
            existingTodo.setPerson(person);
        }

        // Handle attachments
        if (todoDto.attachments() != null && !todoDto.attachments().isEmpty()) {
            // Clear existing attachments
            existingTodo.getAttachments().clear();

            // Add new attachments
            for (AttachmentDto attachmentDto : todoDto.attachments()) {
                Attachment attachment = new Attachment(
                        attachmentDto.fileName(),
                        attachmentDto.fileType(),
                        attachmentDto.data()
                );
                existingTodo.addAttachment(attachment);
            }
        }

        Todo updatedTodo = todoRepository.save(existingTodo);
        return convertToDto(updatedTodo);
    }

    @Override
    public void delete(Long id) {
        Todo existing = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        validateOwnership(existing);
        todoRepository.deleteById(id);
    }

    @Override
    public List<TodoDto> findByPersonId(Long personId) {
        return todoRepository.findByPerson_Id(personId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findOverdueTodos() {
        return todoRepository.findByDueDateBeforeAndCompletedFalse(LocalDateTime.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void validateOwnership(Todo todo) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String todoOwnerUsername = todo.getPerson().getUser().getUsername();

        if (!currentUser.equals(todoOwnerUsername)) {
            throw new AccessDeniedException("You are not the owner of this todo");
        }

    }

    @Override
    public void assignTodo(Long todoId, TodoAssignmentDto dto) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        TodoAssignment assignment = new TodoAssignment();
        assignment.setTodo(todo);
        assignment.setUser(user);
        assignment.setResponsibility(dto.responsibility());

        todoAssignmentRepository.save(assignment);
    }
}
