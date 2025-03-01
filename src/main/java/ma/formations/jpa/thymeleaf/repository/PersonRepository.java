package ma.formations.jpa.thymeleaf.repository;

import jakarta.persistence.criteria.*;
import ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto;
import ma.formations.jpa.thymeleaf.entity.Address;
import ma.formations.jpa.thymeleaf.entity.Person;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface PersonRepository<T> extends JpaRepository<Person, Long>, JpaSpecificationExecutor<T> {
    static Specification<Person> allCriteria(String firstname, String lastname, Integer age, String city) {
        return (person, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (lastname != null && !lastname.trim().equals("")) {
                predicates.add(cb.like(cb.lower(person.get("lastname")), "%" + lastname.toLowerCase() + "%"));
            }

            if (firstname != null && !firstname.trim().equals("")) {
                predicates.add(cb.like(cb.lower(person.get("firstname")), "%" + firstname.toLowerCase() + "%"));
            }

            if (age != 0) {
                predicates.add(cb.equal(person.get("age"), age));
            }

            if (city != null && !city.trim().equals("")) {
                Join<Person, Address> addressJoin = person.join("addresses");
                predicates.add(cb.like(cb.lower(addressJoin.get("city")), "%" + city.toLowerCase() + "%"));
            }

            if (!predicates.isEmpty()) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }
            return cb.conjunction();
        };
    }

    static Specification<Person> byAgeAndCity(Integer minAge, Integer maxAge, String city) {
        return (Root<Person> person, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Joindre l'entité Address
            Join<Person, Address> addressJoin = person.join("addresses");

            // Condition sur l'âge (min et max)
            if (minAge != null) {
                predicates.add(cb.greaterThan(person.get("age"), minAge));
            }
            if (maxAge != null) {
                predicates.add(cb.lessThan(person.get("age"), maxAge));
            }

            // Condition sur la ville (LIKE insensible à la casse)
            if (city != null && !city.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("city")), "%" + city.toLowerCase() + "%"));
            }

            // Ajouter GROUP BY et ORDER BY si nécessaire
            query.groupBy(person.get("firstname"), person.get("lastname"), addressJoin.get("city"));
            query.orderBy(cb.asc(addressJoin.get("city")));

            if (!predicates.isEmpty()) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }
            return cb.conjunction();
        };
    }

    @Query("SELECT new ma.formations.jpa.thymeleaf.dtos.PersonsByCityDto(a.city, COUNT(p)) FROM Person p JOIN p.addresses a GROUP BY a.city")
    List<PersonsByCityDto> countPersonsByCity();


}
