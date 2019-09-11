package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SMS {

//    public static String getPath = "http://apis.haoservice.com/sms/send?mobile=15815043015&tpl_id=5276&tpl_value=%23code%23%3dXY12&key=d1cedb6e328c485c8c5480b15e7ec674";

    public String sendMsg(String code, String telephone){
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try{
            String getPath = "http://apis.haoservice.com/sms/send?mobile=" + telephone + "&tpl_id=5276&tpl_value=%23code%23%3d" + code + "&key=d1cedb6e328c485c8c5480b15e7ec674";
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

}
