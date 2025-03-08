package ma.formations.jpa.thymeleaf.service;

import ma.formations.jpa.thymeleaf.dtos.PersonDto;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IService {
    List<PersonsByCityDto> countPersonsByCity();

    List<PersonDto> personsByAgeAndCity(String minAge, String maxAge, String city);

    void save(PersonDto dto);

    PersonDto findById(Long id) throws Throwable;

    void deleteById(Long id) throws Throwable;

    Page<PersonDto> findAll(String firstname, String lastname, String age, String city, Pageable page);
}
