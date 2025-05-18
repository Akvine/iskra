package ru.akvine.iskra.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.repositories.DictionaryRepository;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.dto.SystemDictionary;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DictionaryLoader {
    private final DictionaryRepository dictionaryRepository;

    @PostConstruct
    public void init() throws IOException {
        String dictionariesFolderName = "dictionaries";
        ClassPathResource resource = new ClassPathResource(dictionariesFolderName);
        File folder = resource.getFile();

        Collection<File> files = FileUtils.listFiles(folder,
                new String[]{"json"},
                true);

        Map<String, File> fileNameWithFile = files.stream()
                .collect(Collectors.toMap(
                        file -> FilenameUtils.removeExtension(file.getName()),
                        file -> file
                ));

        List<String> fileNames = fileNameWithFile.keySet().stream()
                .toList();

        List<String> existedDictionariesNames = dictionaryRepository
                .findAll(fileNames).stream()
                .map(DictionaryEntity::getName)
                .toList();
        List<String> notExistedDictionaries =
                ListUtils.subtract(fileNames, existedDictionariesNames);

        ObjectMapper objectMapper = new ObjectMapper();
        for (String dictionaryName : notExistedDictionaries) {
            SystemDictionary systemDictionary = objectMapper.readValue(
                    fileNameWithFile.get(dictionaryName),
                    SystemDictionary.class
            );

            DictionaryEntity dictionaryToSave = new DictionaryEntity()
                    .setName(dictionaryName)
                    .setDescription(systemDictionary.getDescription())
                    .setValues(String.join(",", systemDictionary.getValues()))
                    .setSystem(true);
            dictionaryRepository.save(dictionaryToSave);
        }
    }
}
