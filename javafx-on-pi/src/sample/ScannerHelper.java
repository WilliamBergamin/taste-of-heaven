package sample;

import com.pi4j.io.gpio.*;


public class ScannerHelper {

    private static final GpioController gpio = GpioFactory.getInstance();
    private static GpioPinDigitalOutput pin;

    static{
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "GPIO for barcode reader", PinState.LOW);
        pin.setShutdownOptions(true, PinState.LOW);
    }

    public static void scannerOn(){
        pin.high();
    }

    public static void scannerOff(){
        pin.low();
    }
}
