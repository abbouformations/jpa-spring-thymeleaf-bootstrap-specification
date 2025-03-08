package ma.formations.jpa.thymeleaf.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonsByCityDto {
    private String city;
    private Long count;
}
