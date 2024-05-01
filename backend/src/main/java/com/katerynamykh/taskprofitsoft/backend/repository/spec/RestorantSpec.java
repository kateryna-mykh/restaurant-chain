package com.katerynamykh.taskprofitsoft.backend.repository.spec;

import com.katerynamykh.taskprofitsoft.backend.dto.restorant.SearchRestorantDto;
import com.katerynamykh.taskprofitsoft.backend.model.Restorant;
import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RestorantSpec {

    public RestorantSpec() {
    }

    public static Specification<Restorant> filterBy(SearchRestorantDto searchDto) {
        return Specification.where(hasLocation(searchDto.address()))
                .and(hasChainId(searchDto.chainId()));
    }

    private static Specification<Restorant> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> location == null || location.isBlank()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(criteriaBuilder.upper(root.get("locationAddress")),
                        "%" + location.toUpperCase() + "%");
    }

    private static Specification<Restorant> hasChainId(Long id) {
   /* 
    *TODO:: rewatch code: works but not as expected    
    * return (root, query, criteriaBuilder) -> {
            if (ids == null || ids.length == 0)
                return criteriaBuilder.conjunction();
            Join<RestorantChain, Restorant> chain = root.join("restorantChain");
            //return root.get("restorantChain").in(Arrays.stream(ids).toArray());
            return chain.get("id").in(Arrays.stream(ids).toArray());
        };*/
        return (root, query, criteriaBuilder) -> {
            if (id == null || id<1)
                return criteriaBuilder.conjunction();
            Join<RestorantChain, Restorant> chain = root.join("restorantChain");
            return criteriaBuilder.equal(chain.get("id"),id);
        };
    }
}
