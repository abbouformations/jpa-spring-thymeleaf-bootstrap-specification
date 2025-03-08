package ma.formations.jpa.thymeleaf;

import ma.formations.jpa.thymeleaf.entity.Address;
import ma.formations.jpa.thymeleaf.entity.Person;
import ma.formations.jpa.thymeleaf.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(PersonRepository dao) {
        return (args -> {
            Person person1 = Person.builder().
                    firstname("Amrani").
                    lastname("Ali").
                    age(10).
                    married(false).build();

            Address address1 = Address.builder().city("rabat").country("Maroc").street("Riad").person(person1).build();
            Address address2 = Address.builder().city("casablanca").country("Maroc").street("Medina").person(person1).build();
            person1.setAddresses(List.of(address1, address2));
            dao.save(person1);

            Person person2 = Person.builder().
                    firstname("Kadiri").
                    lastname("Hassan").
                    age(47).
                    married(true).build();

            Address address3 = Address.builder().city("rabat").country("Maroc").street("Riad").person(person2).build();
            person2.setAddresses(List.of(address3));
            dao.save(person2);

            Person person3 = Person.builder().
                    firstname("Roulam").
                    lastname("RIHAM").
                    age(17).
                    married(false).build();

            Address address5 = Address.builder().city("Tamesna").country("Maroc").street("Majd").person(person3).build();
            person3.setAddresses(List.of(address5));
            dao.save(person3);

            Person person4 = Person.builder().
                    firstname("Ahmadi").
                    lastname("Bilal").
                    age(37).
                    married(true).build();

            Address address6 = Address.builder().city("Tamesna").country("Maroc").street("Majd").person(person4).build();
            person4.setAddresses(List.of(address6));
            dao.save(person4);


            Person person5 = Person.builder().
                    firstname("Kaoutari").
                    lastname("Zakia").
                    age(80).
                    married(true).build();

            Address address7 = Address.builder().city("Fes").country("Maroc").street("Fath").person(person5).build();
            person5.setAddresses(List.of(address7));
            dao.save(person5);

            Person person6 = Person.builder().
                    firstname("Kaoutari").
                    lastname("Zakia").
                    age(80).
                    married(true).build();

            Address address8 = Address.builder().city("Meknes").country("Maroc").street("Annassr").person(person6).build();
            person6.setAddresses(List.of(address8));
            dao.save(person6);

            Person person7 = Person.builder().
                    firstname("Yatribi").
                    lastname("Imad").
                    age(29).
                    married(false).build();

            Address address9 = Address.builder().city("Rabat").country("Maroc").street("Riad").person(person7).build();
            person7.setAddresses(List.of(address9));
            dao.save(person7);
        });
    }

}
