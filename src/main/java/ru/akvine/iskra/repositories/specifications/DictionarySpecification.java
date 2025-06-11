package ru.akvine.iskra.repositories.specifications;

import jakarta.persistence.criteria.Expression;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.repositories.dto.DictionariesFilter;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;

import java.util.Set;

@Component
public class DictionarySpecification {
    public Specification<DictionaryEntity> build(DictionariesFilter filter) {
        Specification<DictionaryEntity> specification = Specification.where(null);

        if (CollectionUtils.isNotEmpty(filter.getNames())) {
            specification = inNames(filter.getNames());
        }

        return specification;
    }

    private Specification<DictionaryEntity> inNames(Set<String> names) {
        return (root, query, criteriaBuilder) -> {
            Expression<String> expression = root.get("name");
            return expression.in(names);
        };
    }
}
