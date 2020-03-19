package sample;

import org.json.JSONObject;

public class BackgroundUpdater extends Thread {
    public void run() {
        while(true) {
            MachineMicrocontrolerHelper.getMicrocontrolerState();
            ServerHelper serverHelper = new ServerHelper();
            JSONObject response = serverHelper.postUpdateStatus();
            if (response == null){
                System.out.println("Something went very wrong");
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
