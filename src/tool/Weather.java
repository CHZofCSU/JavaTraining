package tool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Weather {

    public static String getPath = "http://apis.haoservice.com/weather?cityname=长沙&key=e129161e1f16498cb2b2723433e51e72";

    //返回Json数据的天气信息
    public static String getWeatherData(){

        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try{
            URL url = new URL(getPath);
            URLConnection connection = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String tempStr = "";
            while( (tempStr= bufferedReader.readLine())!=null ){
                stringBuffer.append(tempStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
        return stringBuffer.toString();
    }

    //返回今明两日的气温
    public Map<String,String> getTemperature(){

        Map<String,String> tempMap = new HashMap<String, String>();
        String jsonString = getWeatherData();

        try{
            JSONObject json = JSONObject.fromObject(jsonString);
            JSONObject jsonResult = json.getJSONObject("result");
            JSONObject todayData = jsonResult.getJSONObject("today");
            String todayTemp = todayData.getString("temperature");
            String todayWeather = todayData.getString("weather");

            JSONArray futureData = jsonResult.getJSONArray("future");
            JSONObject tomorrowData = futureData.getJSONObject(0);
            String tomorrowTemp = tomorrowData.getString("temperature");
            String tomorrowWeather = tomorrowData.getString("weather");
            tempMap.put("todayTemp",todayTemp);
            tempMap.put("tomorrowTemp",tomorrowTemp);
            tempMap.put("todayWeather",todayWeather);
            tempMap.put("tomorrowWeather",tomorrowWeather);

        }catch (Exception e){
            e.printStackTrace();
        }
        return tempMap;
    }

//    public static String jsonToString(){
//        String msg = "";
//        String jsonString = "{\"error_code\":0,\"reason\":\"成功\",\"result\":{\"sk\":{\"temp\":\"29\",\"wind_direction\":\"北风\",\"wind_strength\":\"4级\",\"humidity\":\"54\",\"time\":\"16:11\"},\"today\":{\"city\":\"长沙\",\"date_y\":\"2019年09月03日\",\"week\":\"星期二\",\"temperature\":\"22~28\",\"weather\":\"阵雨\",\"fa\":\"03\",\"fb\":\"02\",\"wind\":\"北风 微风\",\"dressing_index\":\"热\",\"dressing_advice\":\"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。\",\"uv_index\":\"弱\",\"comfort_index\":\"--\",\"wash_index\":\"较适宜\",\"travel_index\":\"适宜\",\"exercise_index\":\"适宜\",\"drying_index\":\"--\"},\"future\":[{\"temperature\":\"22~29\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期二\",\"date\":\"20190903\"},{\"temperature\":\"22~30\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期三\",\"date\":\"20190904\"},{\"temperature\":\"23~30\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期四\",\"date\":\"20190905\"},{\"temperature\":\"23~31\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期五\",\"date\":\"20190906\"},{\"temperature\":\"23~32\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期六\",\"date\":\"20190907\"},{\"temperature\":\"23~33\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期日\",\"date\":\"20190908\"},{\"temperature\":\"23~33\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期一\",\"date\":\"20190909\"},{\"temperature\":\"24~34\",\"weather\":\"阴\",\"fa\":\"02\",\"fb\":\"01\",\"wind\":\"东北风 微风\",\"week\":\"星期二\",\"date\":\"20190910\"},{\"temperature\":\"25~34\",\"weather\":\"小雨\",\"fa\":\"07\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期三\",\"date\":\"20190911\"},{\"temperature\":\"25~35\",\"weather\":\"小雨\",\"fa\":\"07\",\"fb\":\"01\",\"wind\":\"北风 微风\",\"week\":\"星期四\",\"date\":\"20190912\"},{\"temperature\":\"27~35\",\"weather\":\"多云\",\"fa\":\"01\",\"fb\":\"07\",\"wind\":\"东南风 微风\",\"week\":\"星期五\",\"date\":\"20190913\"},{\"temperature\":\"27~36\",\"weather\":\"小雨\",\"fa\":\"07\",\"fb\":\"01\",\"wind\":\"南风 微风\",\"week\":\"星期六\",\"date\":\"20190914\"},{\"temperature\":\"27~34\",\"weather\":\"小雨\",\"fa\":\"07\",\"fb\":\"07\",\"wind\":\"东北风 微风\",\"week\":\"星期日\",\"date\":\"20190915\"},{\"temperature\":\"27~35\",\"weather\":\"小雨\",\"fa\":\"07\",\"fb\":\"07\",\"wind\":\"南风 微风\",\"week\":\"星期一\",\"date\":\"20190916\"},{\"temperature\":\"26~34\",\"weather\":\"小雨\",\"fa\":\"07\",\"fb\":\"07\",\"wind\":\"南风 微风\",\"week\":\"星期二\",\"date\":\"20190917\"}]}}";
//        try{
//            JSONObject json = JSONObject.fromObject(jsonString);
//            JSONObject jsonResult = json.getJSONObject("result");
//            JSONObject todayData = jsonResult.getJSONObject("today");
//            String todayTemp = todayData.getString("temperature");
//            JSONArray futureData = jsonResult.getJSONArray("future");
//            JSONObject tomorrowData = futureData.getJSONObject(0);
//            String tomorrowTemp = tomorrowData.getString("temperature");
//
//            msg = todayTemp + "  " + tomorrowTemp;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return msg;
//    }


}
