package ma.formations.jpa.thymeleaf.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    private Long id;
    private String firstname;
    private String lastname;
    private Integer age;
    private boolean married;

    private List<AddressDto> addresses;

    public String getCities() {
        Collector<AddressDto, StringJoiner, String> cityCollector =
                Collector.of(
                        () -> new StringJoiner(","),
                        (sj, p) -> sj.add(p.getCity().toUpperCase()),
                        (sj1, sj2) -> sj1.merge(sj2),
                        StringJoiner::toString);
        String cities = this.addresses
                .stream()
                .collect(cityCollector);

        return cities;
    }
}
