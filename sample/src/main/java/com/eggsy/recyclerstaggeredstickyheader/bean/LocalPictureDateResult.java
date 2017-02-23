package com.eggsy.recyclerstaggeredstickyheader.bean;

import android.content.Context;
import android.content.res.Resources;

import com.eggsy.recyclerstaggeredstickyheader.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;

import static com.eggsy.recyclerstaggeredstickyheader.bean.WrapLocalPictureDetailInfo.DATA_TYPE_TITLE;

/**
 * Created by eggsy on 17-2-10.
 * <p>
 * 保存本地图片日期与图片信息的关系
 * save the relationship between local picture details and picture date string
 */

public class LocalPictureDateResult {

    private Context mContext;

    private DateParseFilter mDateParseFilter;

    /**
     * save the positions of title in the data collections
     */
    int[] mArrayTitlePos;

    /**
     * title collection
     */
    private HashSet<String> titleSet ;

    /**
     * pictures collection,include title and picture details
     */
    ArrayList<WrapLocalPictureDetailInfo> mLocalPictureInfos;

    public LocalPictureDateResult(Context context) {
        this.mContext = context;
        this.mDateParseFilter = new DateParseFilter();
        this.mLocalPictureInfos = new ArrayList<>();
        this.titleSet = new HashSet<>();
        this.mArrayTitlePos = new int[8];
    }

    public ArrayList<WrapLocalPictureDetailInfo> getLocalPictureInfos() {
        return mLocalPictureInfos;
    }

    public int[] getArrayTitlePos() {
        return mArrayTitlePos;
    }

    /**
     * add local picture details
     *
     * @param detailInfo
     */
    public void add(LocalPictureDetailInfo detailInfo) {
        String dateTimeStr = mDateParseFilter.parse(detailInfo.getModifiedDate());
        if (!titleSet.contains(dateTimeStr)) {
            titleSet.add(dateTimeStr);
            if(titleSet.size()> mArrayTitlePos.length){
                int[] newArrayTitlePos = new int[mArrayTitlePos.length*2];
                System.arraycopy(mArrayTitlePos,0,newArrayTitlePos,0, mArrayTitlePos.length);
                mArrayTitlePos = newArrayTitlePos;
            }
            mArrayTitlePos[titleSet.size()-1] = mLocalPictureInfos.size();

            // add title object
            WrapLocalPictureDetailInfo titleInfo = new WrapLocalPictureDetailInfo(dateTimeStr, DATA_TYPE_TITLE);
            mLocalPictureInfos.add(titleInfo);
            // add content object
            WrapLocalPictureDetailInfo contentInfo = new WrapLocalPictureDetailInfo(detailInfo);
            mLocalPictureInfos.add(contentInfo);
        } else {
            mLocalPictureInfos.add(new WrapLocalPictureDetailInfo(detailInfo));
        }

    }

    private class DateParseFilter {

        long minToDayTimeStamp, maxToDayTimeStamp, minYesterdayTimeStamp, minWeekTimeStamp, minMonthTimeStamp, minYearTimeStamp;

        public DateParseFilter() {
            minToDayTimeStamp = getMinToday();
            maxToDayTimeStamp = getMaxToday();
            minYesterdayTimeStamp = getMinYesteday();
            minWeekTimeStamp = getMinWeek();
            minMonthTimeStamp = getMinMonth();
            minYearTimeStamp = getMinYear();
        }

        public String parse(long time) {
            String dateStr = "";
            if (time <= maxToDayTimeStamp && time >= minToDayTimeStamp) {
                dateStr = getString(mContext.getResources(), R.string.title_today);
            } else if (time < minToDayTimeStamp && time >= minYesterdayTimeStamp) {
                dateStr = getString(mContext.getResources(), R.string.title_yesterday);
            } else if (time < minYesterdayTimeStamp && time >= minWeekTimeStamp) {
                dateStr = getDayOfWeekStr(getDayOfWeek(time));
            } else if (time < minWeekTimeStamp && time >= minMonthTimeStamp) {
                dateStr = getString(mContext.getResources(), R.string.title_month);
            } else if (time < minMonthTimeStamp && time >= minYearTimeStamp) {
                dateStr = getString(mContext.getResources(), R.string.title_month_num, getMonth(time) + 1);
            } else if (time < minYearTimeStamp) {
                dateStr = getString(mContext.getResources(), R.string.title_year_num, getYear(time));
            }

            return dateStr;
        }

        /**
         * get year number
         *
         * @param time
         * @return
         */
        public int getYear(long time) {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.YEAR);
        }

        /**
         * get month number
         *
         * @param time
         * @return
         */
        public int getMonth(long time) {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.MONTH);
        }

        /**
         * get day of week
         *
         * @param time
         * @return
         */
        public int getDayOfWeek(long time) {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.DAY_OF_WEEK);
        }

        public String getDayOfWeekStr(int dayOfWeek) {
            String dayOfWeekStr = "";
            switch (dayOfWeek) {
                case 1:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_sunday);
                    break;
                case 2:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_monday);
                    break;
                case 3:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_tuesday);
                    break;
                case 4:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_wednesday);
                    break;
                case 5:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_thursday);
                    break;
                case 6:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_friday);
                    break;
                case 7:
                    dayOfWeekStr = getString(mContext.getResources(), R.string.label_saturday);
                    break;
            }
            return dayOfWeekStr;
        }

        public long getMinToday() {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        public long getMaxToday() {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTimeInMillis();
        }

        public long getMinYesteday() {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        public long getMinWeek() {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getMinimum(Calendar.DAY_OF_WEEK));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        public long getMinMonth() {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        public long getMinYear() {
            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getMinimum(Calendar.DAY_OF_YEAR));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

    }

    public static String getString(Resources resources, int resId) {
        String content = null;
        if (resources != null && resId != 0) {
            content = resources.getString(resId);
        }

        return content;
    }

    public static String getString(Resources resources, int resId, Object... formatArgs) {
        String content = null;
        if (resources != null && resId != 0) {
            content = resources.getString(resId, formatArgs);
        }

        return content;
    }
}
