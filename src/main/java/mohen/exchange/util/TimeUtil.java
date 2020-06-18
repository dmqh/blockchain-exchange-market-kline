package mohen.exchange.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * 日期相关工具类
 *
 * @author yuanqiang.liu
 * @date 2020年4月27日
 */
public class TimeUtil {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final Long ONE_DAY_MS = 86400000L;
    public static final int MS = TimeType.MS.getLength();

    public static final int SECOND = TimeType.SECOND.getLength();

    public static Long parse(String value) throws ParseException {
        return parse(value, TimeType.MS);
    }

    public static Long parse(String value, int timeType) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        return parseLength(sdf.parse(value).getTime(), TimeType.length(timeType));
    }

    public static String parseString(Long value) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        return sdf.format(new Date(parse(value, TimeType.MS)));
    }

    public static Long parse(String value, TimeType timeType) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        return parseLength(sdf.parse(value).getTime(), timeType);
    }

    public static Long parse(Long value, TimeType timeType) {
        return parseLength(value, timeType);
    }


    private static Long parseLength(Long timestamp, TimeType timeType) {
        int length = String.valueOf(timestamp).length();
        int power;
        switch (timeType) {
            case MS:
                power = length - MS;
                break;
            case SECOND:
                power = length - SECOND;
                break;
            default:
                return null;
        }

        int powerAbs = new BigDecimal(power).abs().intValue();
        return power == 0 ? timestamp : power > 0 ? new BigDecimal(timestamp).divide(BigDecimal.TEN.pow(powerAbs)).longValue() : power < 0 ? new BigDecimal(timestamp).multiply(BigDecimal.TEN.pow(powerAbs)).longValue() :
                timestamp;
    }

    /**
     * 获取下一天
     *
     * @param timestamp
     * @return
     */
    public static Long nextDay(long timestamp) throws ParseException {
        return parseLength(timestamp, TimeType.MS) + ONE_DAY_MS;
    }

    public static int dayCount(long start, long end) throws ParseException {
        Long startTime = parse(start, TimeType.MS);
        Long endTime = parse(end, TimeType.MS);

        return Long.valueOf(String.valueOf(((endTime - startTime) / ONE_DAY_MS))).intValue();
    }

    @Getter
    @AllArgsConstructor
    enum TimeType {

        /**
         * 毫秒长度
         */
        MS(13),

        /**
         * 秒长度
         */
        SECOND(10);

        private int length;

        protected static TimeType length(int length) {
            return Optional.ofNullable(Arrays.stream(values()).filter(o -> o.getLength() == length).findAny().get()).orElseThrow(NullPointerException::new);
        }
    }

}