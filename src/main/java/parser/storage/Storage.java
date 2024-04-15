package parser.storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Storage {
    public static final ConcurrentMap<String, Long> parsedResultMap = new ConcurrentHashMap<>();

    public static void add(String key) {
        if (!parsedResultMap.containsKey(key)) {
            parsedResultMap.put(key, 1L);
        } else {
            parsedResultMap.put(key, parsedResultMap.get(key) + 1);
        }
    }
}
