package ma.formations.jpa.thymeleaf.repository;

import jakarta.persistence.criteria.Join;
import ma.formations.jpa.thymeleaf.entity.Address;
import ma.formations.jpa.thymeleaf.entity.Person;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class PersonSpecifications {

    public static Specification<Person> hasLastnameContaining(String lastname) {
        return (person, cq, cb) -> {
            if (lastname != null && !lastname.trim().isEmpty()) {
                return cb.and(cb.like(cb.lower(person.get("lastname")), "%" + lastname.toLowerCase() + "%"));
            }
            return cb.conjunction();
        };
    }

    public static Specification<Person> hasFirstnameContaining(String firstname) {
        return (person, cq, cb) -> {
            if (firstname != null && !firstname.trim().isEmpty()) {
                return cb.and(cb.like(cb.lower(person.get("firstname")), "%" + firstname.toLowerCase() + "%"));
            }
            return cb.conjunction();
        };
    }

    public static Specification<Person> hasAge(String age) {
        return (person, cq, cb) -> {
            Integer ageInt = Optional.ofNullable(age)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> {
                        try {
                            return Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }).orElse(null);

            if (ageInt == null) {
                return cb.conjunction();
            }

            return cb.equal(person.get("age"), ageInt);
        };
    }


    public static Specification<Person> hasCityContaining(String city) {
        return (person, cq, cb) -> {
            if (city != null && !city.trim().isEmpty()) {
                Join<Person, Address> addressJoin = person.join("addresses");
                return cb.and(cb.like(cb.lower(addressJoin.get("city")), "%" + city.toLowerCase() + "%"));
            }
            return cb.conjunction();
        };
    }

    public static Specification<Person> byAgeLessThan(String maxAge) {
        return (person, cq, cb) -> {
            Integer maxAgeInt = Optional.ofNullable(maxAge)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> {
                        try {
                            return Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }).orElse(null);

            if (maxAgeInt == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(person.get("age"), maxAgeInt);
        };
    }

    public static Specification<Person> byAgeGreaterThan(String minAge) {
        return (person, cq, cb) -> {
            Integer minAgeInt = Optional.ofNullable(minAge)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> {
                        try {
                            return Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }).orElse(null);

            if (minAgeInt == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(person.get("age"), minAgeInt);
        };
    }
}
