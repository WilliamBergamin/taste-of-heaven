package sample;

import com.pi4j.io.gpio.*;
import javafx.concurrent.Task;


public class ScannerHelper {

    private static final GpioController gpio = GpioFactory.getInstance();
    private static GpioPinDigitalOutput pin;
    private static boolean isOn = false;

    static{
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "GPIO for barcode reader", PinState.LOW);
        pin.setShutdownOptions(true, PinState.LOW);
        Task<Void> scannerHandler = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
               while(true) {
                   if (isOn) {
                       pin.high();
                   } else {
                       pin.low();
                   }
                   try {
                       Thread.sleep(1000);
                       pin.low();
                       Thread.sleep(50);
                   } catch (InterruptedException e) {
                   }
               }
            }
        };
        new Thread(scannerHandler).start();
    }

    public static void scannerOn(){ isOn = true; }

    public static void scannerOff(){ isOn = false; }
}
