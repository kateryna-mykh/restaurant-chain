package parser.dto;

import java.util.List;

public record RestaurantDto (
    String locationAddress,
    String manager,
    Integer seetsCapacity,
    Integer employeesNumber,
    List<String> menuItems,
    String restaurantChain) {
}
