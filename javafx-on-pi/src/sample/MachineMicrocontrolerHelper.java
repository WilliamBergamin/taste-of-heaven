package sample;

import com.pi4j.io.serial.*;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class MachineMicrocontrolerHelper {

    private static final Serial serial;
    private static SerialConfig config;
    private static String microState;
    private static String location;
    private static Semaphore mutex;
    private static Map<Byte, String> possibleMicroState;

    static{
        mutex = new Semaphore(1);
        possibleMicroState = new HashMap<Byte, String>();
        possibleMicroState.put((byte) 1, "idle");
        possibleMicroState.put((byte) 2, "processing");
        possibleMicroState.put((byte) 3, "done");
        serial = SerialFactory.createInstance();
        config = new SerialConfig();
    }

    public static void init(){
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not get read from the receive buffer, the
                // buffer will continue to grow and consume memory.
                try {
                    mutex.acquire();
                    // byte meaning
                    // 2: status
                    // 1: water sensors
                    // 0: water sensors
                    System.out.println("[HEX DATA]   " + event.getHexByteString());
                    byte[] received = event.getByteBuffer().array();
                    MachineMicrocontrolerHelper.microState = possibleMicroState.getOrDefault(received[2], "error");
                    System.out.print("state:"+MachineMicrocontrolerHelper.microState);
                    if (MachineMicrocontrolerHelper.microState == "error"){
                        Machine.setState((short) 2);
                        Machine.setError("Problem with micro controller");
                    }
                    // TODO add more information on sensor level
                    // if bottom sensors are not all 1 then put machine in empty mode
                    if ((received[0] & (byte) 31) != (byte) 31){
                        // set state of machine to empty
                        Machine.setState((short) 1);
                    }else{
                        // set state of machine to ok
                        Machine.setState((short) 0);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                }
            }
        });
        config.device(RaspberryPiSerial.S0_COM_PORT)
                .baud(Baud._230400)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        try {
            serial.open(config);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" ==>> SERIAL SETUP FAILED : " + e.getMessage());
        }
    }

    public static String getMicrocontrolerState() {
        try {
            mutex.acquire();
            return microState;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
        return null;
    }

    private static byte[] getCommandFromOrder(String mixer, String alcohol, boolean big){
        byte message[] = {0,0,0,0,0};
        switch(alcohol) {
            case "Rum":
                message[0] = big ? (byte) 2 : (byte) 1;
                break;
            case "Vodka":
                message[1] = big ? (byte) 2 : (byte) 1;
                break;
            case "Gin":
                message[2] = big ? (byte) 2 : (byte) 1;
                break;
            default:
                // "None" do nothing
        }
        switch(mixer) {
            case "Orange juice":
                message[3] = (byte) 1;
                break;
            case "Apple juice":
                message[4] = (byte) 1;
                break;
            default:
                // "None" do nothing
        }
        return message;
    }

    public static void sendNewOrder(JSONObject orderData) {
        /*
        expected data for drink
         {
              "mixer_type": "",
              "alcohol_type": "",
              "double": true,
              "price": 3.5
         }
        */
        try {
            mutex.acquire();
            System.out.println("writting to micro");
            // write a byte array to the serial transmit buffer
            serial.write(getCommandFromOrder(orderData.getString("mixer_type"),
                                             orderData.getString("alcohol_type"),
                                             orderData.getBoolean("double")));

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }

    }


}
