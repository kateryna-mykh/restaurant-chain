package parser.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import parser.dto.ItemDto;
import parser.storage.Storage;

@ExtendWith(MockitoExtension.class)
class StorageToItemDtoListConverterTest {
    @InjectMocks
    private StorageToItemDtoListConverter converter;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Storage.parsedResultMap.put("Salad", 2L);
        Storage.parsedResultMap.put("Lemonade", 1L);
        Storage.parsedResultMap.put("Burger", 5L);
        Storage.parsedResultMap.put("French Fries", 5L);
        Storage.parsedResultMap.put("Pizza", 2L);
    }
    
    @Test
    void convert_ReturnSortedListDto() {
        List<ItemDto> expected = new ArrayList<>();
        expected.add(new ItemDto("Burger", 5L));
        expected.add(new ItemDto("French Fries", 5L));
        expected.add(new ItemDto("Pizza", 2L));
        expected.add(new ItemDto("Salad", 2L));
        expected.add(new ItemDto("Lemonade", 1L));
       
        List<ItemDto> actual = converter.convert();
        Assert.assertEquals(expected, actual);
    }
}
