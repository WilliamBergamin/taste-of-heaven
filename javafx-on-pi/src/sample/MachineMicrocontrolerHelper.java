package sample;

import com.pi4j.io.serial.*;
import netscape.javascript.JSObject;

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
        possibleMicroState.put((byte) 0x01, "idle");
        possibleMicroState.put((byte) 0x02, "pouring");
        possibleMicroState.put((byte) 0x03, "done");
        possibleMicroState.put((byte) 0x04, "busy");
        possibleMicroState.put((byte) 0x05, "error");
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

                // print out the data received to the console
                try {
                    System.out.println("[HEX DATA]   " + event.getHexByteString());
                    System.out.println("[ASCII DATA] " + event.getAsciiString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        config.device(RaspberryPiSerial.S0_COM_PORT)
                .baud(Baud._9600)
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

    public static void sendNewOrder(JSObject orderData) {
        try {
            mutex.acquire();
            // write a formatted string to the serial transmit buffer
            serial.write("CURRENT TIME: " + new Date().toString());

            // write a individual bytes to the serial transmit buffer
            serial.write((byte) 13);
            serial.write((byte) 10);

            // write a simple string to the serial transmit buffer
            serial.write("Second Line");

            // write a individual characters to the serial transmit buffer
            serial.write('\r');
            serial.write('\n');

            // write a string terminating with CR+LF to the serial transmit buffer
            serial.writeln("Third Line");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }

    }


}
