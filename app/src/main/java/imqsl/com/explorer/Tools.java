package imqsl.com.explorer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tools{

    public static List<Map<String, Object>> getList(String[] menuNameArray, int[] menuResourceArray) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < menuNameArray.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text_item", menuNameArray[i]);
            map.put("image_item", menuResourceArray[i]);
            data.add(map);
        }

        return data;
    }
    public static String getJsonArray(String myurl,String param){
        try {
            URL url=new URL(myurl);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            DataOutputStream data=new DataOutputStream(urlConnection.getOutputStream());
            data.writeBytes(param);
            data.flush();
            data.close();
            if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
               String stringline=null;
                StringBuilder sb=new StringBuilder();
                while ((stringline=br.readLine())!=null){
                    sb.append(stringline);
                }
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
           System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return null;
    }
    public static boolean isOnline(Context context){
        ConnectivityManager connMgr= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

}
