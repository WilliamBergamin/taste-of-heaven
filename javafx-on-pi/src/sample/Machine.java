package sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Machine {

    private static String machineToken;
    private static String eventKey;
    private static String machineKey;
    private static ArrayList<String> selectedOrder;
    private static ArrayList<String> processedOrders;
    private static String state;
    private static String error;
    private static String location;
    private static BackgroundUpdater backgroundUpdater;

    static{
        selectedOrder = new ArrayList<String>();
        processedOrders = new ArrayList<String>();
        backgroundUpdater = new BackgroundUpdater();
    }

    public static void initializeFromJSON(JSONObject json) throws JSONException {
        Machine.state = json.getString("state");
        Machine.machineKey = json.getString("machine_key");
        Machine.error = json.getString("error");
        Machine.selectedOrder = new ArrayList<String>();
        Machine.processedOrders = new ArrayList<String>();
        JSONArray tmpJSONArray = json.getJSONArray("selected_order");
        for (int i=0;i<tmpJSONArray.length();i++){
            Machine.selectedOrder.add(tmpJSONArray.get(i).toString());
        }
        tmpJSONArray = json.getJSONArray("processed_orders");
        for (int i=0;i<tmpJSONArray.length();i++){
            Machine.processedOrders.add(tmpJSONArray.get(i).toString());
        }
        Machine.backgroundUpdater.start();
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        Machine.location = location;
    }

    public static String getMachineKey() {
        return machineKey;
    }

    public static void setMachineKey(String machineKey) {
        Machine.machineKey = machineKey;
    }

    public static String getMachineToken() {
        return machineToken;
    }

    public static String getEventKey() {
        return eventKey;
    }

    public static ArrayList<String> getSelectedOrder() {
        return selectedOrder;
    }

    public static ArrayList<String> getProcessedOrders() {
        return processedOrders;
    }

    public static String getState() {
        return state;
    }

    public static String getError() {
        return error;
    }

    public static void setMachineToken(String machineToken) {
        Machine.machineToken = machineToken;
    }

    public static void setEventKey(String eventKey) {
        Machine.eventKey = eventKey;
    }

    public static void setSelectedOrder(ArrayList<String> selectedOrder) {
        Machine.selectedOrder = selectedOrder;
    }

    public static void setProcessedOrders(ArrayList<String> processedOrders) {
        Machine.processedOrders = processedOrders;
    }

    public static void setState(String state) {
        Machine.state = state;
    }

    public static void setError(String error) {
        Machine.error = error;
    }
}
