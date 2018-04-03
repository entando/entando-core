package com.agiletec.plugins.jacms.aps.system.services.contentmodel.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class ContentModelUtils {

    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT_TYPE = "contentType";
    private static final String KEY_DESCR = "descr";

    public static class Predicates {

        public static Predicate<ContentModel> byId(Long id) {
            return p -> p.getId() == id;
        }

        public static Predicate<ContentModel> byContentType(String contentType) {
            return p -> p.getContentType().equalsIgnoreCase(contentType);
        }

        public static Predicate<ContentModel> byDescrLike(String descrToken) {
            return p -> p.getDescription().toLowerCase().contains(descrToken.toLowerCase());
        }

    }

    public static class Comparators {

        public static Comparator<ContentModel> byId() {
            return new Comparator<ContentModel>() {

                @Override
                public int compare(ContentModel o1, ContentModel o2) {
                    return Long.compare(o1.getId(), o2.getId());
                }
            };
        }

        public static Comparator<ContentModel> byContentType() {
            return new Comparator<ContentModel>() {

                @Override
                public int compare(ContentModel o1, ContentModel o2) {
                    return o1.getContentType().compareTo(o2.getContentType());
                }
            };
        }

        public static Comparator<ContentModel> byDescr() {
            return new Comparator<ContentModel>() {

                @Override
                public int compare(ContentModel o1, ContentModel o2) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            };
        }

    }


    public static Comparator<ContentModel> getComparator(String sort) {
        switch (sort) {
            case KEY_ID:
                return Comparators.byId();
            case KEY_CONTENT_TYPE:
                return Comparators.byContentType();
            case KEY_DESCR:
                return Comparators.byDescr();
            default:
                return Comparators.byId();
        }
    }

    public static Comparator<ContentModel> getComparator(String sort, String direction) {
        Comparator<ContentModel> comparator = getComparator(sort);
        if (null == comparator) {
            return null;
        }
        if (direction.equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    public static Predicate<ContentModel> getPredicate(Filter filter) {
        if (null == filter) {
            return null;
        }
        switch (filter.getAttribute()) {
            case KEY_ID:
                return Predicates.byId(Long.valueOf(filter.getValue()));
            case KEY_CONTENT_TYPE:
                return Predicates.byContentType(filter.getValue());
            case KEY_DESCR:
                return Predicates.byDescrLike(filter.getValue());
            default:
                break;
        }
        return null;
    }

    public static List<Predicate<ContentModel>> getPredicates(RestListRequest restListRequest) {
        List<Predicate<ContentModel>> predicates = new ArrayList<>();
        if (null == restListRequest || null == restListRequest.getFilters()) {
            return predicates;
        }
        for (Filter f : restListRequest.getFilters()) {
            Predicate<ContentModel> p = getPredicate(f);
            if (null != p) {
                predicates.add(p);
            }
        }
        return predicates;
    }

}

