package ru.akvine.iskra.rest.validators;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.constants.ApiErrorCodes;
import ru.akvine.iskra.exceptions.ValidationException;
import ru.akvine.iskra.rest.dto.configuration.column.CreateConfigurationRequest;

@Component
public class ColumnConfigurationValidator {

    @Value("${max.filters.per.configuration}")
    private int maxFilters;

    @Value("${max.post.filters.per.configuration}")
    private int maxPostFilters;

    public void verifyCreateConfigurationRequest(CreateConfigurationRequest request) {
        Asserts.isNotNull(request);

        if (CollectionUtils.isNotEmpty(request.getConverters())
                && request.getConverters().size() > maxFilters) {
            String errorMessage = String.format(
                    "Filters count = [%s] can't be more than max = [%s]",
                    request.getConverters().size(), maxFilters);
            throw new ValidationException(ApiErrorCodes.Validation.FILTERS_MAX_COUNT_ERROR, errorMessage);
        }

        if (CollectionUtils.isNotEmpty(request.getPostConverters())
                && request.getPostConverters().size() > maxPostFilters) {
            String errorMessage = String.format(
                    "Post filters count = [%s] can't be more than max = [%s]",
                    request.getConverters().size(), maxFilters);
            throw new ValidationException(ApiErrorCodes.Validation.FILTERS_MAX_COUNT_ERROR, errorMessage);
        }
    }
}
