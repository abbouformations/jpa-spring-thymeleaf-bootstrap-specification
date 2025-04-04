package ma.formations.jpa.thymeleaf.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonsByAgeAndCityDto {
    private String firstname;
    private String lastname;
    private String city;
    private int age;

}
