package Sound;

import javax.sound.sampled.*;

/**
 * 这个类可用作蜂鸣器 <br/>
 * 函数中的 Hz变量肯定不是频率单位赫兹，因为这个值升高时声音的频率反而降低，暂不知道是什么（也许代码作者搞反了？） <br/>
 * 据测试，为确保蜂鸣可聆听到，Hz变化范围至多是：14000~16000
 */
public class Buzzer {
    public static float SAMPLE_RATE = 8000f;

    public static void tone(int Hz, int mSecs, double vol) throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(SAMPLE_RATE,8,1,true,false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();

        for (int i = 0; i < mSecs * 8; i++) {
            double angle = i / (SAMPLE_RATE / Hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public static void main(String[] args) throws Exception {
        Buzzer.tone(15000, 800, 0.2);

        for (int i = 14000; i < 16000; i += 20) {
            Buzzer.tone(i, 200, 0.1);
            System.out.println(i);
        }

    }
}