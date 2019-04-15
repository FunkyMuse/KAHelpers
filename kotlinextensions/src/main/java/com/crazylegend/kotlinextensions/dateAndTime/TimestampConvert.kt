package com.crazylegend.kotlinextensions.dateAndTime


/**
 * Created by hristijan on 4/15/19 to long live and prosper !
 */

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimestampConvert {

    /**
     * EEE, dd MMM yyyy HH:mm:ss zzz
     */
    private val rfc1123Formatter: SimpleDateFormat

    /**
     * EEEEEEEEE, dd-MMM-yy HH:mm:ss zzz
     */
    private val rfc1036Formatter: SimpleDateFormat

    /**
     * EEE MMM dd HH:mm:ss yyyy
     */
    private val asctimeFormatter: SimpleDateFormat

    /**
     * yyyy-MM-dd'T'HH:mm:ssXXX
     */

    /**
     * Source: [https://en.wikipedia.org/wiki/ISO_8601](https://en.wikipedia.org/wiki/ISO_8601)
     * <pre>
     * expressed according to ISO 8601:
     * Date: 2018-01-31
     * Combined date and time in UTC: 2018-01-31T18:13:41+00:00
     * 2018-01-31T18:13:41Z
     * 20180131T181341Z
     * Week: 2018-W05
     * Date with week number: 2018-W05-3
     * Ordinal date: 2018-031
    </pre> *
     */
    private val iso8601Formatter: SimpleDateFormat

    val EEE_DD_MMM_YYYY_HH_MM_SS_ZZZ = "EEE, dd MMM yyyy HH:mm:ss zzz"

    val EEEEEEEEE_DD_MMM_YY_HH_MM_SS_ZZZ = "EEEEEEEEE, dd-MMM-yy HH:mm:ss zzz"

    val EEE_MMM_DD_HH_MM_SS_YYYY = "EEE MMM dd HH:mm:ss yyyy"

    val YYYY_MM_DD_T_HH_MM_SSZZZZZ = "yyyy-MM-dd'T'HH:mm:ssZZZZZ" // works on all android versions but not in unit tests



    private val DATEFORMAT = "yyyy-MM-dd HH:mm:ss"

    fun iso8601Format(): String {
        return YYYY_MM_DD_T_HH_MM_SSZZZZZ
    }

    init {
        rfc1123Formatter = SimpleDateFormat(EEE_DD_MMM_YYYY_HH_MM_SS_ZZZ, Locale.ENGLISH)
        rfc1123Formatter.timeZone = TimeZone.getTimeZone("GMT")

        rfc1036Formatter = SimpleDateFormat(EEEEEEEEE_DD_MMM_YY_HH_MM_SS_ZZZ, Locale.ENGLISH)
        rfc1036Formatter.timeZone = TimeZone.getTimeZone("GMT")

        asctimeFormatter = SimpleDateFormat(EEE_MMM_DD_HH_MM_SS_YYYY, Locale.ENGLISH)
        asctimeFormatter.timeZone = TimeZone.getTimeZone("GMT")

        //        iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        iso8601Formatter = SimpleDateFormat(iso8601Format(), Locale.ENGLISH)
        // asctimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Converts an ISO-8601-style timestamp to a Date.
     */
    @Throws(ParseException::class)
    fun iso8601ToDate(timestamp: String): Date {
        return iso8601Formatter.parse(timestamp)
    }

    /**
     * Converts a Date to an ISO-8601-style timestamp.
     */
    fun dateToIso8601(date: Date): String {
        return iso8601Formatter.format(date)
    }

    /**
     * Converts an RFC-1123-style timestamp to a Date.
     */
    @Throws(ParseException::class)
    fun rfc1123ToDate(timestamp: String): Date {
        return rfc1123Formatter.parse(timestamp)
    }

    /**
     * Converts a Date to an RFC-1123-style timestamp.
     */
    fun dateToRfc1123(date: Date): String {
        return rfc1123Formatter.format(date)
    }

    /**
     * Converts an RFC-1036-style timestamp to a Date.
     */
    @Throws(ParseException::class)
    fun rfc1036ToDate(timestamp: String): Date {
        return rfc1036Formatter.parse(timestamp)
    }

    /**
     * Converts a Date to an RFC-1036-style timestamp.
     */
    fun dateToRfc1036(date: Date): String {
        return rfc1036Formatter.format(date)
    }

    /**
     * Converts an asctime-style timestamp to a Date.
     */
    @Throws(ParseException::class)
    fun asctimeToDate(timestamp: String): Date {
        return asctimeFormatter.parse(timestamp)
    }

    /**
     * Converts a Date to an asctime-style timestamp.
     */
    fun dateToAsctime(date: Date): String {
        return asctimeFormatter.format(date)
    }

    /**
     * Converts an HTTP-style timestamp to a Date. The timestamp could be any of
     * RFC 1123, RFC 1036, or C's asctime().
     *
     * @throws ParseException if none of the formats apply.
     */
    @Throws(ParseException::class)
    fun httpToDate(timestamp: String): Date {

        // Try the different formats in order of preference

        return try {
            rfc1123ToDate(timestamp)
        } catch (e: ParseException) {
            try {
                rfc1036ToDate(timestamp)
            } catch (e1: ParseException) {
                asctimeToDate(timestamp)
            }

        }
    }

    fun GetUTCDateTimeAsDate(date: Date): Date? {
        //note: doesn't check for null
        return StringDateToDate(GetUTCDateTimeAsString(date))
    }

    fun GetUTCDateTimeAsString(date: Date): String {
        val sdf = SimpleDateFormat(DATEFORMAT)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return sdf.format(date)
    }

    fun StringDateToDate(StrDate: String): Date? {
        var dateToReturn: Date? = null
        val dateFormat = SimpleDateFormat(DATEFORMAT)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            dateToReturn = dateFormat.parse(StrDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateToReturn
    }

}
