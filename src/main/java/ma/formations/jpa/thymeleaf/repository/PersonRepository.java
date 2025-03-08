package ma.formations.jpa.thymeleaf.repository;

import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import ma.formations.jpa.thymeleaf.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository<T> extends JpaRepository<Person, Long>, JpaSpecificationExecutor<T> {
    @Query("SELECT new ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto(a.city, COUNT(p)) FROM Person p JOIN p.addresses a GROUP BY a.city")
    List<PersonsByCityDto> countPersonsByCity();
}
