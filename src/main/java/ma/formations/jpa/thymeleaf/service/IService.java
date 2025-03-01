package ma.formations.jpa.thymeleaf.service;

import ma.formations.jpa.thymeleaf.dtos.PersonDto;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;

import java.util.List;

public interface IService {
    List<PersonsByCityDto> countPersonsByCity();

    List<PersonDto> personsByAgeAndCity(Integer minAge, Integer maxAge, String city);

    void save(PersonDto dto);

    PersonDto findById(Long id) throws Throwable;

    void deleteById(Long id) throws Throwable;

    List<PersonDto> findAll(String firstname, String lastname, Integer age, String city);
}
