package cr.ac.una.sistemafichas.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString()); // formato: 2026-04-08T20:30:00
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String value = in.nextString();
        return LocalDateTime.parse(value);
    }
}
