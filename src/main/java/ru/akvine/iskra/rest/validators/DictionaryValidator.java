package ru.akvine.iskra.rest.validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.constants.ApiErrorCodes;
import ru.akvine.iskra.exceptions.ValidationException;
import ru.akvine.iskra.utils.FileUtils;

@Component
public class DictionaryValidator {
    @Value("${dictionaries.elements.max.count}")
    private int maxElementsCount;

    @Value("${dictionaries.import.files.extensions.supports}")
    private Set<String> supportedExtensions = new HashSet<>();

    public void verifyCreateDictionaryRequest(MultipartFile file) {
        Asserts.isNotNull(file, "file is null");

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!supportedExtensions.contains(extension)) {
            throw new ValidationException(
                    ApiErrorCodes.Validation.FILE_EXTENSION_NOT_SUPPORTED,
                    "File extension = [" + extension + "] is not supported by app!");
        }

        List<String> extractedElements = FileUtils.parseValues(FileUtils.extractInputStream(file));
        verifyElementsCount(extractedElements.size());
    }

    private void verifyElementsCount(int elementsCount) {
        if (elementsCount > maxElementsCount) {
            String errorMessage = String.format(
                    "Elements count = [%s] is more than max available = [%s]", elementsCount, maxElementsCount);
            throw new ValidationException(ApiErrorCodes.Validation.ELEMENTS_MAX_COUNT_ERROR, errorMessage);
        }
    }
}
