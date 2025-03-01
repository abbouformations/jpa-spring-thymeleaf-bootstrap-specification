package ma.formations.jpa.thymeleaf.service;

import lombok.AllArgsConstructor;
import ma.formations.jpa.thymeleaf.dtos.PersonDto;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import ma.formations.jpa.thymeleaf.entity.Person;
import ma.formations.jpa.thymeleaf.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ma.formations.jpa.thymeleaf.repository.PersonRepository.allCriteria;
import static ma.formations.jpa.thymeleaf.repository.PersonRepository.byAgeAndCity;

@Service
@Transactional
@AllArgsConstructor
public class ServiceImpl implements IService {
    //For using Spring Data and JPA Specifications API
    private PersonRepository personRepository;
    private ModelMapper modelMapper;


    @Override
    public List<PersonsByCityDto> countPersonsByCity() {
        return personRepository.countPersonsByCity();
    }

    @Override
    public List<PersonDto> personsByAgeAndCity(Integer minAge, Integer maxAge, String city) {
        return personRepository.findAll(byAgeAndCity(minAge, maxAge, city)).stream().map(
                bo -> modelMapper.map(bo, PersonDto.class)).toList();
    }

    @Override
    public void save(PersonDto dto) {
        personRepository.save(modelMapper.map(dto, Person.class));
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
    }

    @Override
    public List<PersonDto> findAll(String firstname, String lastname, Integer age, String city) {
        return personRepository.
                findAll(allCriteria(firstname, lastname, age, city)).
                stream().
                map(bo -> modelMapper.map(bo, PersonDto.class)).
                toList();
    }
}
