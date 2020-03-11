package sample;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class MachineHelper {

    private static final Serial serial;
    private static SerialConfig config;
    private static Semaphore mutex;

    static{
        mutex = new Semaphore(1);
        serial = SerialFactory.createInstance();
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                try {
                    System.out.println(event.getHexByteString());
                    System.out.println(event.getAsciiString());
                    //TODO set a single listener that masks what gets received and set the appropiatre values
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        config = new SerialConfig();
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

    public static void getStatus() {
        try {
            mutex.acquire();
            // retrrun the status
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }

    }

    public static void sendNewOrder() {
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
