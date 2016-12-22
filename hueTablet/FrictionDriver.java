package hueTablet;

import com.pi4j.io.i2c.*;
import com.pi4j.io.gpio.*;

import java.io.IOException;

public class FrictionDriver {
    
    static long frequency1 = 33090;
    static long frequency2 = 21500;
    //static double voltage = 4.95;
    
    //static I2CBus bus;
    //static mcp4725 dac;
    static FrequencyController freq;
    
    static final GpioController gpio = GpioFactory.getInstance();
    
    // 0 = Electrovibration, 1 = Ultrasonic, 2 = Hybrid
    private int frictionMode;
    
    public FrictionDriver(int mode) {
        frictionMode = mode;
        try {
            //Setup I2C communication with MCP4725
            //bus = I2CFactory.getInstance(I2CBus.BUS_1);
            //dac = new mcp4725(bus);
        
            //setup signals
            freq = new FrequencyController(gpio);
        
            //start with everything off
            disableAll();
        } catch (Exception e) {}
    }
    
    public void setFrictionLow() throws Exception {
        if (frictionMode == 0) {
            // turn off EV signal
            freq.reset(2);
        } else if (frictionMode == 1) {
            // turn on UFM signal
            freq.setFrequency(frequency1,1);
        } else {
            // turn on UFM signal and turn off EV signal
            freq.setFrequency(frequency1,1);
            freq.reset(2);
        }
    }
    
    public void setFrictionHigh() throws Exception {
        if (frictionMode == 0) {
            // turn ON EV signal
            freq.setFrequency(frequency2,2);
        } else if (frictionMode == 1) {
            // turn OFF UFM signal
            freq.reset(1);
        } else {
            // turn OFF UFM signal and turn ON EV signal
            freq.reset(1);
            freq.setFrequency(frequency2,2);
        }
        
    }
    
    public static void disableAll() throws Exception {
        freq.reset(1);
        freq.reset(2);
        
    }
    
    public static void main (String [] args) throws Exception {
        FrictionDriver driver = new FrictionDriver(2);
        driver.setFrictionHigh();
        
    }
}
