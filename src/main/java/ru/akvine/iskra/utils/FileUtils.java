package ru.akvine.iskra.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.iskra.exceptions.FileProcessException;

@UtilityClass
public class FileUtils {
    public List<String> parseValues(InputStream inputStream) {
        List<String> parsedValues = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parsedValues.add(line);
            }
        } catch (IOException exception) {
            String errorMessage =
                    String.format("Some error was occurred while parse file = [%s]", exception.getMessage());
            throw new FileProcessException(errorMessage);
        }

        return parsedValues;
    }

    public InputStream extractInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException exception) {
            String errorMessage = String.format(
                    "Some error was occurred while extract input stream from file. Message = [%s]",
                    exception.getMessage());
            throw new FileProcessException(errorMessage);
        }
    }
}
