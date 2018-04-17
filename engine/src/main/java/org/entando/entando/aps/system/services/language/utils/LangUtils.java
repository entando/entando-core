package org.entando.entando.aps.system.services.language.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.language.LanguageDto;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class LangUtils {

    private static final String KEY_CODE = "code";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ACTIVE = "isActive";
    private static final String KEY_DEFAULT = "isDefault";

    public static class Predicates {

        public static Predicate<LanguageDto> byCode(String code) {
            return p -> p.getCode().equalsIgnoreCase(code);
        }

        public static Predicate<LanguageDto> byDescriptionLike(String descrToken) {
            return p -> p.getDescription().toLowerCase().contains(descrToken.toLowerCase());
        }

        public static Predicate<LanguageDto> byDefault(boolean value) {
            return p -> p.isDefaultLang() == value;
        }

        public static Predicate<LanguageDto> byActive(boolean value) {
            return p -> p.isActive() == value;
        }

    }

    public static class Comparators {

        public static Comparator<LanguageDto> byCode() {
            return new Comparator<LanguageDto>() {

                @Override
                public int compare(LanguageDto o1, LanguageDto o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
            };
        }

        public static Comparator<LanguageDto> byDefault() {
            return new Comparator<LanguageDto>() {

                @Override
                public int compare(LanguageDto o1, LanguageDto o2) {
                    return Boolean.compare(o1.isDefaultLang(), o2.isDefaultLang());
                }
            };
        }

        public static Comparator<LanguageDto> byActive() {
            return new Comparator<LanguageDto>() {

                @Override
                public int compare(LanguageDto o1, LanguageDto o2) {
                    return Boolean.compare(o1.isActive(), o2.isActive());
                }
            };
        }

        public static Comparator<LanguageDto> byDescription() {
            return new Comparator<LanguageDto>() {

                @Override
                public int compare(LanguageDto o1, LanguageDto o2) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            };
        }

    }


    public static Comparator<LanguageDto> getComparator(String sort) {
        switch (sort) {
            case KEY_CODE:
                return Comparators.byCode();
            case KEY_DESCRIPTION:
                return Comparators.byDescription();
            case KEY_ACTIVE:
                return Comparators.byActive();
            case KEY_DEFAULT:
                return Comparators.byDefault();
            default:
                return Comparators.byCode();
        }
    }

    public static Comparator<LanguageDto> getComparator(String sort, String direction) {
        Comparator<LanguageDto> comparator = getComparator(sort);
        if (null == comparator) {
            return null;
        }
        if (direction.equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    public static Predicate<LanguageDto> getPredicate(Filter filter) {
        if (null == filter) {
            return null;
        }
        switch (filter.getAttribute()) {
            case KEY_CODE:
                return Predicates.byCode(filter.getValue());
            case KEY_DESCRIPTION:
                return Predicates.byDescriptionLike(filter.getValue());
            case KEY_ACTIVE:
                return Predicates.byActive(Boolean.valueOf(filter.getValue()));
            case KEY_DEFAULT:
                return Predicates.byDefault(Boolean.valueOf(filter.getValue()));

            default:
                break;
        }
        return null;
    }

    public static List<Predicate<LanguageDto>> getPredicates(RestListRequest restListRequest) {
        List<Predicate<LanguageDto>> predicates = new ArrayList<>();
        if (null == restListRequest || null == restListRequest.getFilters()) {
            return predicates;
        }
        for (Filter f : restListRequest.getFilters()) {
            Predicate<LanguageDto> p = getPredicate(f);
            if (null != p) {
                predicates.add(p);
            }
        }
        return predicates;
    }

}

