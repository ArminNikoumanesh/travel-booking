package com.armin.utility.bl.time;

import com.armin.utility.bl.DateTime;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Jalali Service Class,
 * Containing Methods about Jalali Calendar
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class JalaliService {
    private final int[] gregorianDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final int[] jalaliDays = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    public Timestamp jalaliToTimeStamp(Integer year, Integer month, Integer day) {
        DateTime dateTime = this.jalaliToGregorian(year, month, day);
        GregorianCalendar calendar = new GregorianCalendar(dateTime.getYear(), dateTime.getMonth() - 1, dateTime.getDay());
        return new Timestamp(calendar.getTimeInMillis());
    }

    public Timestamp jalaliToTimeStamp(int year, int month, int day
            , int hour, int minute, int seconds) {
        DateTime dateTime = this.jalaliToGregorian(year, month, day);
        GregorianCalendar calendar = new GregorianCalendar(dateTime.getYear(), dateTime.getMonth() - 1, dateTime.getDay(), hour, minute, seconds);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public DateTime timestampToJalali(Timestamp timestamp) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(timestamp.getTime()));
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        return this.gregorianToJalali(year, month + 1, day);
    }

    public DateTime gregorianToJalali(int year, int month, int day) {
        int gy = year - 1600;
        int gm = month - 1;
        int gd = day - 1;

        int g_day_no = 365 * gy + Math.floorDiv(gy + 3, 4) - Math.floorDiv(gy + 99, 100) + Math.floorDiv(gy + 399, 400);
        for (int i = 0; i < gm; ++i) {
            g_day_no += this.gregorianDays[i];
        }
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) {
            g_day_no++;
        }
        g_day_no += gd;
        int j_day_no = g_day_no - 79;

        int j_np = Math.floorDiv(j_day_no, 12053); // 12053 = 365*33 + 32/4
        j_day_no = j_day_no % 12053;

        int jy = 979 + 33 * j_np + 4 * Math.floorDiv(j_day_no, 1461); // 1461 = 365*4 + 4/4
        j_day_no %= 1461;
        if (j_day_no >= 366) {
            jy += Math.floorDiv(j_day_no - 1, 365);
            j_day_no = (j_day_no - 1) % 365;
        }

        int i;
        for (i = 0; i < 11 && j_day_no >= this.jalaliDays[i]; ++i) {
            j_day_no -= this.jalaliDays[i];
        }
        int jm = i + 1;
        int jd = j_day_no + 1;

        return new DateTime(jy, jm, jd);
    }

    public DateTime jalaliToGregorian(int year, int month, int day) {
        int jy = year - 979;
        int jm = month - 1;
        int jd = day - 1;

        int j_day_no = 365 * jy + Math.floorDiv(jy, 33) * 8 + Math.floorDiv(jy % 33 + 3, 4);
        for (int i = 0; i < jm; ++i) {
            j_day_no += this.jalaliDays[i];
        }
        j_day_no += jd;
        int g_day_no = j_day_no + 79;

        int gy = 1600 + 400 * Math.floorDiv(g_day_no, 146097); // 146097 = 365*400 + 400/4 - 400/100 + 400/400
        g_day_no = g_day_no % 146097;

        boolean leap = true;
        if (g_day_no >= 36525) { // 36525 = 365*100 + 100/4
            g_day_no--;
            gy += 100 * Math.floorDiv(g_day_no, 36524); // 36524 = 365*100 + 100/4 - 100/100
            g_day_no = g_day_no % 36524;

            if (g_day_no >= 365) {
                g_day_no++;
            } else {
                leap = false;
            }
        }

        gy += 4 * Math.floorDiv(g_day_no, 1461); // 1461 = 365*4 + 4/4
        g_day_no %= 1461;
        if (g_day_no >= 366) {
            leap = false;

            g_day_no--;
            gy += Math.floorDiv(g_day_no, 365);
            g_day_no = g_day_no % 365;
        }

        int i;
        for (i = 0; g_day_no >= this.gregorianDays[i] + ((i == 1 && leap) ? 1 : 0); i++) {
            g_day_no -= this.gregorianDays[i] + ((i == 1 && leap) ? 1 : 0);
        }
        int gm = i + 1;
        int gd = g_day_no + 1;

        return new DateTime(gy, gm, gd);
    }

    public LocalDateTime getTimeFromJalaliInfo(int year, int month, int day, int hours, int minutes, int seconds) {
        LocalDateTime result = jalaliToTimeStamp(year, month, day, hours, minutes, seconds).toLocalDateTime();
        if (month > 6) {
            return result.minusHours(3).minusMinutes(30);
        } else {
            return result.minusHours(4).minusMinutes(30);
        }
    }
}
