package parser.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import parser.Main;
import parser.dto.RestorantDto;
import parser.exception.IncorrectParamsException;
import parser.storage.Storage;

public class ProcessJsonFile implements Runnable {
    private final ObjectMapper jsonMapper;
    private final File file;

    public ProcessJsonFile(File file) {
        this.file = new File(file.getPath());
        this.jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * Method for pasing json single objects and json arrays
     */
    @Override
    public void run() {
        try (JsonParser parser = jsonMapper.getFactory().createParser(file)) {
            if (parser.nextToken() == JsonToken.START_ARRAY) {
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    parseRestoarntDto(parser);
                }
            } else {
                parseRestoarntDto(parser);
            }
        } catch (IOException e) {
            throw new RuntimeException("Parsing go wrong");
        }
    }

    private void parseRestoarntDto(JsonParser parser)
            throws StreamReadException, DatabindException, IOException {
        RestorantDto obj = jsonMapper.readValue(parser, RestorantDto.class);
        Field declaredField;
        List<String> value = new ArrayList<>();
        try {
            declaredField = obj.getClass().getDeclaredField(Main.atribute);
            declaredField.setAccessible(true);

            if (Collection.class.isAssignableFrom(declaredField.getType())) {
                List<String> list = Stream.of(declaredField.get(obj).toString()
                        .replaceAll("[\\p{Ps}\\p{Pe}]", "").split(",")).map(String::trim).toList();
                value.addAll(list);
            } else {
                value.add(String.valueOf(declaredField.get(obj)));
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                | SecurityException e) {
            throw new IncorrectParamsException("Atribute value is incorrect = " + Main.atribute);
        }
        for (String v : value) {
            Storage.add(v);
        }
    }
}
