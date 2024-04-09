package parser.model;

import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Restorant {
    private Long id;
    private String locationAddress;
    private String manager;
    private Integer seetsCapacity;
    private Integer employeesNumber;
    private List<String> menu;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private RestorantChain restorantChain;
}
