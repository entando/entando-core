package org.entando.entando.web.common.model;

import java.util.Date;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;
import org.apache.commons.lang3.StringUtils;

public class DateRange {

    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public DateRange() {

    }

    public DateRange(String input) {
        this.decode(input);
    }

    private void decode(String input) {
        if (StringUtils.isBlank(input)) {
            return;
        }

        boolean isFormatted = input.startsWith("[") && input.endsWith("]");
        if (!isFormatted) {
            return;
        }

        input = StringUtils.substringBetween(input, "[", "]");
        String[] dates = input.split(" TO ");
        if (dates.length != 2) {
            return;
        }
        this.setStart(this.parse(dates[0]));
        this.setEnd(this.parse(dates[1]));
    }

    private Date parse(String timestamp) {
        if (StringUtils.isBlank(timestamp) || timestamp.equals("*")) {
            return null;
        }

        String pattern = SystemConstants.API_DATE_FORMAT;
        Date ddd = DateConverter.parseDate(timestamp, pattern);

        return ddd;
    }
}
