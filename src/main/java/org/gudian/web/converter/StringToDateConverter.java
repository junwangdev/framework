package org.gudian.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author GJW
 * 日期类型转换器，将string类型的日期格式转换为Date类型
 * time: 2021/1/4 16:13
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {

    private static ThreadLocal<SimpleDateFormat[]> formats = new ThreadLocal<SimpleDateFormat[]>() {
        @Override
        protected SimpleDateFormat[] initialValue() {
            return new SimpleDateFormat[]{
                    new SimpleDateFormat("yyyy-MM"),
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy-MM-dd HH"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            };
        }
    };

    @Override
    public Date convert(String source) {
        if (source == null || source.trim().equals("")) {
            return null;
        }

        Date result = null;
        String originalValue = source.trim();
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formats.get()[0]);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formats.get()[1]);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}$")) {
            return parseDate(source, formats.get()[2]);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get()[3]);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get()[4]);
        } else if (originalValue.matches("^\\d{1,13}$")) {
            try {
                long timeStamp = Long.parseLong(originalValue);
                if (originalValue.length() > 10) {
                    result = new Date(timeStamp);
                } else {
                    result = new Date(1000L * timeStamp);
                }
            } catch (Exception e) {
                result = null;
                e.printStackTrace();
            }
        } else {
            result = null;
        }

        return result;
    }

    /**
     * 格式化日期
     *
     * @param dateStr    String 字符型日期
     * @param dateFormat 日期格式化器
     * @return Date 日期
     */
    public Date parseDate(String dateStr, DateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }
}
