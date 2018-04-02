package com.evanhoe.tango.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.evanhoe.tango.TangoApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    public final static String APK_MIME_TYPE = "application/vnd.android.package-archive";

//	This function is no longer being used.
//	/**
//	 * performs a get request to strUrl, returns response
//	 * @param urlStr
//	 * @return
//	 * @throws IOException
//	 */
//	public static String httpGet(String strUrl) throws IOException {
//        URL url = new URL(strUrl);
//        HttpURLConnection conn
//                = (HttpURLConnection) url.openConnection();
//
//        if (conn.getResponseCode() != 200) {
//            throw new IOException(conn.getResponseMessage());
//        }
//
//        // Buffer the result into a string
//        BufferedReader rd = new BufferedReader(
//                new InputStreamReader(conn.getInputStream()));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//
//        conn.disconnect();
//        return sb.toString();
//    }


    /**
     * performs a post request to strUrl with strPostData, returns response
     * @param strUrl
     * @param strPostData
     * @return
     * @throws IOException
     */
    public static String sendPost(String strUrl, String strPostData) throws IOException {

        URL url = new URL(strUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        String input = strPostData;

        // This **RETURNS** a ByteBuffer, that we aren't receiving. Should we receive
        // the result and send into os.write() through ByteBuffer.array(), or just
        // drop this altogether.
        //Charset.forName("UTF-8").encode(input);

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuilder sb = new StringBuilder();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }
    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    public static String sendGet(String strUrl, JSONObject postDataParams)  {
        try {
            URL url;

            url = new URL(strUrl);
            final java.net.HttpURLConnection  conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();




            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            StringBuilder sb = new StringBuilder();
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                sb.append(output);
            }

            br.close();
            conn.disconnect();

            return sb.toString();

        }

        //return "503";


        catch (Exception e){
            String ss=e.getMessage();

            return ss;

        }


        //return "error 503";
    }


    public static String saveData(String strUrl, JSONObject postDataParams,String token) throws Exception {

        URL url = new URL(strUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        String auth="Bearer  "+token;
        conn.setRequestProperty("Authorization", auth);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        //  conn.setRequestProperty("Content-Type", "application/json");


        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(postDataParams));

        writer.flush();
        writer.close();
        os.close();

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuilder sb = new StringBuilder();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }



    public static String sendGet1(String strUrl, String token) throws Exception {

        URL url = new URL(strUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        String auth="Bearer  "+token;
        conn.setRequestProperty("Authorization", auth);
        conn.setDoOutput(false);
        conn.setDoInput(true);
        //  conn.setConnectTimeout(1500);
        //      conn.setConnectTimeout(1500);
        // conn.setRequestProperty("Content-Type", "application/json");

        // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuilder sb = new StringBuilder();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }

    /**
     * performs a post request to strUrl with strPostData, returns binary response
     * @param strUrl
     * @param strPostData
     * @return
     * @throws IOException
     */
    public static String sendPostBinaryResponse(String strUrl, String strPostData) throws IOException
    {
        URL url = new URL(strUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        String input = strPostData;

        // This **RETURNS** a ByteBuffer, that we aren't receiving. Should we receive
        // the result and send into os.write() through ByteBuffer.array(), or just
        // drop this altogether.
        //Charset.forName("UTF-8").encode(input);

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        if ( conn.getResponseCode() != 200 )
        {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        if ( ! conn.getHeaderField("Content-type").equals ( APK_MIME_TYPE ) )
        {
            throw new RuntimeException ( "Failed: Invalid APK type." );
        }

        // Get the download directory
        File downloadDir = Environment.getExternalStoragePublicDirectory ( Environment.DIRECTORY_DOWNLOADS );
        if ( ! downloadDir.exists() )
        {
            downloadDir.mkdirs();
        }

        // Create the filename for the APK file
        File apkFileName = new File ( downloadDir, "TermidorHP.apk" );

        // Open the output file
        OutputStream os2 = new FileOutputStream ( apkFileName );
        // Get the input stream
        InputStream is = conn.getInputStream();

        // Allocate the space needed
        byte[] data = new byte[8096];

        int readLen = -1;
        while ( (readLen=is.read ( data )) != -1 )
        {
            os2.write ( data, 0, readLen );
        }

        is.close();
        os2.close();

        conn.disconnect();
        return apkFileName.toString();
    }
    public static String saveData2(String strUrl, String postDataParams, String token) throws Exception {

        URL url = new URL(strUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        String auth="Bearer  "+token;
        conn.setRequestProperty("Authorization", auth);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");


        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(postDataParams);

        writer.flush();
        writer.close();
        os.close();

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuilder sb = new StringBuilder();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            sb.append(output);
        }

        conn.disconnect();
        return sb.toString();
    }
}
