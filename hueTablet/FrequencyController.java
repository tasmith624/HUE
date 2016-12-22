package hueTablet;

import com.pi4j.wiringpi.Spi;
import com.pi4j.io.gpio.*;

public class FrequencyController {
    
    final Pin CE0 = RaspiPin.GPIO_10;       // Define output pin for Channel 1 (Piezos)
    final Pin CE1 = RaspiPin.GPIO_11;       // Define output pin for Channel 2 (Piezos)
    
    public static GpioPinDigitalOutput FSYNC_CHANNEL1;
    public static GpioPinDigitalOutput FSYNC_CHANNEL2;
   
    public FrequencyController(GpioController controller) {
        // setup SPI communication on Rasperry Pi
        int fd0 = Spi.wiringPiSPISetupMode(0, 10000000, 2);
        int fd1 = Spi.wiringPiSPISetupMode(1, 10000000, 2);
        
        if (fd0 <= -1 || fd1 <= -1) {
            System.out.println("SPI SETUP FAILED");
            return;
        }
        
        // provision selector pins for the two AD9833 chips
        FSYNC_CHANNEL1 = controller.provisionDigitalOutputPin(CE0, PinState.HIGH);
        FSYNC_CHANNEL2 = controller.provisionDigitalOutputPin(CE1, PinState.HIGH);
        
        //force the selector pins HIGH on shutdown
        FSYNC_CHANNEL1.setShutdownOptions(true, PinState.HIGH);
        FSYNC_CHANNEL2.setShutdownOptions(true, PinState.HIGH);
        
    
    }

    
    public static void setFrequency(long frequency, int channel) throws Exception {
        
        int refFreq = 25000000;
        long freqWord = (long) ((frequency * Math.pow(2,28)) / refFreq);
        
        int MSB = (int) ((freqWord & 0xFFFC000) >> 14); //Only lower 14 bits used for data
        int LSB = (int) (freqWord & 0x3FFF);
        
        //Set control bits 15 and 14 to 0 and 1 respectively, for frequency register 0
        LSB |= 0x4000;
        MSB |= 0x4000;
        
        writeRegister(0x2100, channel);
        writeRegister(LSB, channel);             //Write lower 16 bits to AD9833 registers
        writeRegister(MSB, channel);             //Write upper 16 bits to AD9833 registers
        writeRegister(0xC000, channel);          //Phase register
        writeRegister(0x2000, channel);          //exit and set to sine wave output
    }
    
    public static byte lowByte(int value) {
        byte low = (byte) (value & 0xFF);
        return low;
        
    }
    
    public static byte highByte(int value) {
        byte high = (byte) ((value >>> 8) & 0xFF);
        return high;
    }
    
    private static void writeRegister(int data, int channel) throws Exception {
        
        byte packet[] = new byte[2];
        
        packet[0] = highByte(data);
        packet[1] = lowByte(data);
        
        if (channel == 1) {
            FSYNC_CHANNEL2.high();
            FSYNC_CHANNEL1.low();
            Spi.wiringPiSPIDataRW(0, packet, 2);
            FSYNC_CHANNEL1.high();
        } else {
            FSYNC_CHANNEL1.high();
            FSYNC_CHANNEL2.low();
            Spi.wiringPiSPIDataRW(1, packet, 2);
            FSYNC_CHANNEL2.high();
        }
        
       
        
    }
    
    public static void reset(int channel) throws Exception {
        
        writeRegister(0x100,channel);
        //Thread.sleep(10);
        
    }
    
    public void shutdown(GpioController controller) {
        controller.shutdown();
        controller.unprovisionPin(FSYNC_CHANNEL1);
        controller.unprovisionPin(FSYNC_CHANNEL2);
    }
    
     public static void main(String args[]) throws Exception {
        
       final GpioController gpio = GpioFactory.getInstance();
    
        FrequencyController freq = new FrequencyController(gpio);
        
        long frequency = (long) 21500;
        
        freq.reset(1);
        freq.reset(2);
       
        freq.setFrequency(frequency, 1);
        freq.setFrequency(frequency, 2);
        
        
        freq.shutdown(gpio);
        
    }
        
    
}
