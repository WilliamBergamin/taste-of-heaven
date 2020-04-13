package sample;

import org.json.JSONObject;

public class BackgroundUpdater extends Thread {
    public void run() {
        while(true) {
            ServerHelper serverHelper = new ServerHelper();
            System.out.println("machine state sent: "+Machine.getState());
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
