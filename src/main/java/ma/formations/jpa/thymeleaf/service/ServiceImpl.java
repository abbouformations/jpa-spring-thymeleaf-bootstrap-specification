package ma.formations.jpa.thymeleaf.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.formations.jpa.thymeleaf.dtos.PersonDto;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import ma.formations.jpa.thymeleaf.entity.Person;
import ma.formations.jpa.thymeleaf.repository.PersonRepository;
import ma.formations.jpa.thymeleaf.repository.PersonSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ServiceImpl implements IService {
    //For using Spring Data and JPA Specifications API
    private PersonRepository personRepository;
    private ModelMapper modelMapper;

    @Override
    public List<PersonsByCityDto> countPersonsByCity() {
        return personRepository.countPersonsByCity();
    }

    @Override
    public List<PersonDto> personsByAgeAndCity(String minAge, String maxAge, String city) {
        Specification<Person> spec = Specification.where(PersonSpecifications.byAgeLessThan(maxAge)).
                and(PersonSpecifications.byAgeGreaterThan(minAge)).
                and(PersonSpecifications.hasCityContaining(city));

        return personRepository.findAll(spec).stream().map(
                bo -> modelMapper.map(bo, PersonDto.class)).toList();
    }

    @Override
    public void save(PersonDto dto) {
        personRepository.save(modelMapper.map(dto, Person.class));
        log.info("############the person {} is saved with success", dto);
    }

    @Override
    public PersonDto findById(Long id) throws Throwable {
        return modelMapper.map(personRepository.findById(id).orElseThrow(
                        () -> new RuntimeException(String.format("No person with ID [%s] exist !", id))),
                PersonDto.class);
    }

    @Override
    public void deleteById(Long id) throws Throwable {
        if (id == null)
            throw new RuntimeException("Enter a correct id person");
        Person personFound = (Person) personRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("No person with ID [%s] exist !", id)));
        personRepository.delete(personFound);
        log.info("############the person ID {} is removed with success", id);
    }

    @Override
    public Page<PersonDto> findAll(String firstname, String lastname, String age, String city, Pageable page) {
        Specification<Person> spec = Specification.where(PersonSpecifications.hasAge(age)).
                and(PersonSpecifications.hasFirstnameContaining(firstname)).
                and(PersonSpecifications.hasLastnameContaining(lastname)).
                and(PersonSpecifications.hasCityContaining(city));

        Page boListPage = personRepository.findAll(spec, page);
        List PersonDtoList = boListPage.getContent().stream().
                map(bo -> modelMapper.map(bo, PersonDto.class)).toList();
        return new PageImpl<>(PersonDtoList, boListPage.getPageable(), boListPage.getTotalElements());
    }
}
