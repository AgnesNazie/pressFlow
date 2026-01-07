package se.lexicon.pressflow.service;

import se.lexicon.pressflow.dto.PersonDto;
import se.lexicon.pressflow.dto.PersonRegistrationDto;
import se.lexicon.pressflow.entity.Role;

import java.util.List;

public interface PersonService {

        List<PersonDto> findAll();

        PersonDto findById(Long id);

        PersonDto create(PersonRegistrationDto personDto);


        PersonDto update(Long id, PersonDto personDto);

        void delete(Long id);

        PersonDto findByEmail(String email);


        void updatePassword(Long id, String newPassword);

        void toggleExpired(Long id, boolean status);

        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        void addRole(Long id, Role role);

        void removeRole(Long id, Role role);



}
