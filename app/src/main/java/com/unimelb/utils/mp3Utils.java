package com.unimelb.utils;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.base.CharMatcher;
import com.unimelb.data.Record;
import com.unimelb.data.RecordComparator;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;


public class mp3Utils {
    private static BigInteger bigval = null;
    private static int intval;
    private static final int ONE_DAY = 24 * 60 * 60 * 1000;
    private RequestQueue queue;
    BigInteger count = BigInteger.ONE;

    /**
     * @param str
     * @return
     */
    public static BigInteger string2bigint(String str) {
        bigval = new BigInteger(str);
        return bigval;
    }

    /**
     * @param str
     * @return
     */
    public static int string2int(String str) {
        intval = Integer.parseInt(str);
        return intval;
    }

    public static String getCurrTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    public static String getHistoryTimeRange() {
        Date date = new Date();
        DateFormat dfl = new SimpleDateFormat("yyyyMMddHH0000");
        DateFormat dfr = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 1);
        String rightBound =  dfr.format(c.getTime());
        c.setTime(date);
        c.add(Calendar.HOUR, -10);
        String leftBound =  dfl.format(c.getTime());
        return leftBound + "," + rightBound;
    }

    public static String getDateHourRelSomeDate(String dateStr, int param) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHH");
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, param);
        return df.format(c.getTime());
    }

    /**
     * get some day's datetime, step is n * day
     * @param param
     * @return
     */
    public static String getDateTimeRelCurrTime(int param) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, param);
        return df.format(c.getTime());
    }

    public BigInteger getCount() {
        return count;
    }

    /**
     * @param context
     * @param url
     */
//    public String doHttpQuery(final Context context, String url, Map<String, String> params) {
//        if (queue == null) {
//            queue = Volley.newRequestQueue(context);
//        }
//        RequestQueue queue = Volley.newRequestQueue(context);
//        String fixedUrl = fixQueryUrl(url, params);
//        //fixedUrl = "http://49.213.15.196/mp3-iot-service/serena/temperature20150921031706/20150921031707";
//        fixedUrl = "http://49.213.15.196/mp3-iot-service/serena/temperature/get/20151005092014/20151011063428";
//        StringRequest stringReq = new StringRequest(Request.Method.POST, fixedUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//						//debugMsg(context, "response str:" + response.toString());
//                        //debugMsg(context, "size-in:"+req.size());
//                        //return response.toString();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                debugMsg(context, "failed to do volley request.");
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(stringReq);
//    }

    /**
     * @param context
     * @param url
     */
    public void doHttpUpdate(final Context context, String url,  Map<String, String> params) {
        try {
            if (queue == null) {
                queue = Volley.newRequestQueue(context);
            }
            String fixedUrl = fixUpdateUrl(url, params);
            String idx = params.get("idx");
            StringRequest stringReq = new StringRequest(Request.Method.POST, fixedUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            debugMsg(context, "update state:" + response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //debugMsg(context, "failed to do volley request." + error.getMessage());
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringReq);
            count = count.add(BigInteger.ONE);
            debugMsg(context, "task queue [" + count + "]: added");
        } catch (Exception e) {
            debugMsg(context, "failed to do voley request.");
            return;
        }
    }

    /**
     * Display debug message on app's screen
     * @param context
     * @param str
     */
    public static void debugMsg(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public static String fixQueryUrl(String url, Map<String, String> params) {
        if (params.size() < 2) return url;
        return url + "/" + params.get("stime") + "/" + params.get("etime");
    }


    /**
     * @param url
     * @param params
     * @return
     */
    public String fixUpdateUrl(String url, Map<String, String> params) {
       if (params.size() < 2) return null;
        return url + "/" + params.get("submit_time") + "/" + params.get("value");
    }

    public static String cleanNonPrintableChars(String s) {
        String printable = CharMatcher.INVISIBLE.removeFrom(s);
        String clean = CharMatcher.ASCII.retainFrom(printable).replaceAll("[><=]", "");;
        return clean;
    }

    /**
     * sort record list by date.
     * @param list
     */
    public void sortRecordsByDate(List<Record> list) {
        Collections.sort(list, new RecordComparator());
    }

    public static void main(String [] args) {
        mp3Utils util = new mp3Utils();
        System.out.println(getDateHourRelSomeDate("2015101900", -1));
        // query acc
    }

}
