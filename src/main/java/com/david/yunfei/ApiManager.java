package com.david.yunfei;

import android.util.Log;

import com.david.yunfei.entities.PrdtResult;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.ning.http.client.*;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * api 接口类
 * <p/>
 * <p/>
 * Created by davidleen29 onFieldDataChangeListener 14-2-26.
 */
public class ApiManager {

    public static final String TAG = ApiManager.class.getSimpleName();

    private static ApiManager ourInstance = new ApiManager();

    public static ApiManager getInstance() {
        return ourInstance;
    }


    private ApiManager() {
    }

    //客户端连接
    public static AsyncHttpClient client;

    static {
        //设置链接参数   默认超时时间6秒
        client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setConnectionTimeoutInMs(6000).build());

    }

    public List<PrdtResult> readPrdt(String prd_no,boolean like) throws InterruptedException, ExecutionException, IOException {


        String URL_BASE="http://"+HttpUrl.IPAddress+":"+HttpUrl.IPPort+"/"+HttpUrl.ServiceName;

        String URL_EQUAL=URL_BASE+"/api/prdts/equal?prd_no=%s";
        String URL_Like=URL_BASE+"/api/prdts/like?prd_no=%s";




       String   url=String.format(like?URL_Like:URL_EQUAL, URLEncoder.encode(prd_no) );



    //    String url="http://www.baidu.com";
        return remoteInvoke(url, null,


                new AsyncHandler<List<PrdtResult>>() {
                    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                    @Override
                    public STATE onStatusReceived(HttpResponseStatus status) throws Exception {


                        int statusCode = status.getStatusCode();
                        // The Status have been read
                        // If you don't want to read the headers,body or stop processing the response
                        if (statusCode >= 500) {
                            return STATE.ABORT;
                        }
                        return STATE.CONTINUE;
                    }

                    @Override
                    public STATE onHeadersReceived(HttpResponseHeaders h) throws Exception {
                        //  Headers headers = h.getHeaders();
                        // The headers have been read
                        // If you don't want to read the body, or stop processing the response

                        return STATE.CONTINUE;
                    }

                    @Override
                    public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {

                        bytes.write(bodyPart.getBodyPartBytes());

                        return STATE.CONTINUE;
                    }

                    @Override
                    public List<PrdtResult> onCompleted() throws Exception {
                        // Will be invoked once the response has been fully read or a ResponseComplete exception
                        // has been thrown.
                        // NOTE: should probably use Content-Encoding from headers

                        String responseResult = bytes.toString(HTTP.UTF_8);
                        bytes.close();
                        Type listType = new TypeToken<ArrayList<PrdtResult>>() {
                        }.getType();
                        List<PrdtResult> result = new Gson().fromJson(responseResult, listType);
                        return result;


                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        t.printStackTrace();
                    }
                }

        );


    }

//
//    /**
//     * 解析返回结果
//     *
//     * @param responseString
//     * @return List<JSONObject>
//     *
//     */
//    private static <T> List<T>   resolveData(String responseString,Class<T> classType)  {
//
//
//
//
//
//
////
////        Gson gson=new Gson();
////
////       List<JsonElement> element= gson.fromJson(responseString,List.class);
////        for(JsonElement s:element)
////        {
////
////            result.add(    gson.fromJson(s,classType));
////        }
//
//        return result;
//
//
//
//    }


    /**
     * 执行 远程调用   捕获异常 重新抛出
     *
     * @param url
     * @param body
     * @param handler
     * @return T
     *
     */
    private <T> T remoteInvoke(String url, String body, AsyncHandler<T> handler ) throws IOException, ExecutionException, InterruptedException {


        Log.d(TAG, "发送请求：         url=" + url + "  post:  " + body);



            AsyncHttpClient.BoundRequestBuilder builder;
            if (body == null)
                builder = client.prepareGet(url);
            else {
                builder = client.preparePost(url);
                builder.setBody(body);

            }


            T result = builder.execute(handler).get();

            Log.d(TAG, "接收数据 ：     " + result);
            return result
                    ;

    }

}