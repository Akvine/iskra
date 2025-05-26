package ru.akvine.iskra.rest.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.constants.ApiErrorCodes;
import ru.akvine.iskra.exceptions.ValidationException;
import ru.akvine.iskra.rest.dto.configuration.table.CreateTableConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.table.UpdateTableConfigurationRequest;

@Component
public class TableConfigurationValidator {
    @Value("${generation.max.batch.size}")
    private int maxBatchSize;

    public void verifyCreateTableConfigurationRequest(CreateTableConfigurationRequest request) {
        verify(request.getRowsCount(), request.getBatchSize());
        verify(request.getBatchSize());
    }

    public void verifyUpdateTableConfigurationRequest(UpdateTableConfigurationRequest request) {
        if (request.getRowsCount() != null && request.getBatchSize() != null) {
            verify(request.getRowsCount(), request.getBatchSize());
        }

        if (request.getBatchSize() != null) {
            verify(request.getBatchSize());
        }
    }

    private void verify(int rowsCount, int batchSize) {
        if (rowsCount < batchSize) {
            String errorMessage = String.format(
                    "Rows count = [%s] can't be less than batch size = [%s]",
                    rowsCount, batchSize
            );
            throw new ValidationException(
                    ApiErrorCodes.Configuration.TABLE_CONFIGURATION_ERROR,
                    errorMessage
            );
        }
    }

    private void verify(int batchSize) {
        if (batchSize > maxBatchSize) {
            String errorMessage = String.format(
                    "Request batch size = [%s] can't be more than max limit batch size = [%s]",
                    batchSize, maxBatchSize
            );
            throw new ValidationException(
                    ApiErrorCodes.Configuration.TABLE_CONFIGURATION_ERROR,
                    errorMessage
            );
        }
    }
}
