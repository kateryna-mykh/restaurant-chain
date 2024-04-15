package parser.service.impl;

import java.util.List;
import java.util.Map;
import parser.dto.ItemDto;
import parser.storage.Storage;

public class StorageToItemDtoListConverter {
    public List<ItemDto> convert() {
        return Storage.parsedResultMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.<String, Long>comparingByKey()))
                .map(m -> new ItemDto(m.getKey(), m.getValue()))
                .toList();
    }
}
