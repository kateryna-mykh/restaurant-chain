package com.katerynamykh.taskprofitsoft.backend.repository.spec;

import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.SearchRestaurantDto;
import com.katerynamykh.taskprofitsoft.backend.model.Restaurant;
import com.katerynamykh.taskprofitsoft.backend.model.RestaurantChain;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpec {

    public RestaurantSpec() {
    }

    public static Specification<Restaurant> filterBy(SearchRestaurantDto searchDto) {
        return Specification.where(hasLocation(searchDto.address()))
                .and(hasChainId(searchDto.chainId()));
    }

    private static Specification<Restaurant> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> location == null || location.isBlank()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(criteriaBuilder.upper(root.get("locationAddress")),
                        "%" + location.toUpperCase() + "%");
    }

    private static Specification<Restaurant> hasChainId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id<1)
                return criteriaBuilder.conjunction();
            Join<RestaurantChain, Restaurant> chain = root.join("restaurantChain");
            return criteriaBuilder.equal(chain.get("id"),id);
        };
    }
}
