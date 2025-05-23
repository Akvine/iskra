package ru.akvine.iskra.rest.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.constants.ApiErrorCodes;
import ru.akvine.iskra.exceptions.ValidationException;
import ru.akvine.iskra.rest.dto.configuration.table.CreateTableConfigurationRequest;

@Component
public class TableConfigurationValidator {
    @Value("${generation.max.batch.size}")
    private int maxBatchSize;

    public void verifyCreateTableConfigurationRequest(CreateTableConfigurationRequest request) {
        if (request.getRowsCount() < request.getBatchSize()) {
            String errorMessage = String.format(
                    "Rows count = [%s] can't be less than batch size = [%s]",
                    request.getRowsCount(), request.getBatchSize()
                    );
            throw new ValidationException(
                    ApiErrorCodes.Configuration.TABLE_CONFIGURATION_ERROR,
                    errorMessage
            );
        }

        if (request.getBatchSize() > maxBatchSize) {
            String errorMessage = String.format(
                    "Request batch size = [%s] can't be more than max limit batch size = [%s]",
                    request.getBatchSize(), maxBatchSize
            );
            throw new ValidationException(
                    ApiErrorCodes.Configuration.TABLE_CONFIGURATION_ERROR,
                    errorMessage
            );
        }
    }
}
