package parser.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RestorantChain {
    private Long id;
    private String name;
    private String cuisine;
    private Integer totalBranches;
    private BigDecimal annualRevenue;
    @Builder.Default
    private Set<Restorant> branches = new HashSet<>();
}
