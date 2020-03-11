package sample;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerHelper {

    private final static String baseURL = "http://3.133.81.46:80";
    public ServerHelper(){}

    public  static JSONObject getMachineData(String MachineToken){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(baseURL +"/api/v1/machine/");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Authorization","Token "+MachineToken);

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static JSONObject postUpdateStatus(){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(baseURL +"/api/v1/machine/");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Authorization","Token "+Machine.getMachineToken());

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            JSONObject obj = new JSONObject();

            obj.put("state", Machine.getState());
            obj.put("error", Machine.getError());
            obj.put("location", Machine.getLocation());

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes(obj.toString());
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }


    public static JSONObject getOrderInEvent(String orderKey){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(baseURL +"/api/v1/machine/order/"+Machine.getEventKey()+"/"+orderKey);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization","Token "+Machine.getMachineToken());

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static JSONObject postOrderCompleted(){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(baseURL +"/api/v1/machine/order/done/"+Machine.getEventKey());
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Authorization","Token "+Machine.getMachineToken());

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
