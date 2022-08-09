/*
 * Charset Detector, MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class CharsetDetector {

    static final String RU_ENCODERS = "x-UTF-16LE-BOM,UTF-16BE,x-UTF-32LE-BOM,x-UTF-32BE-BOM,UTF-8,windows-1251,cp866,koi8-r";
    static final String RU_FREQUENCY = "оеаитнсрвлкмдпуяыгзбчйчъжьюшцщэфёОЕАИТНСРВЛКМДПУЯЫГЗБЧЙЧЪЖЬЮШЦЩЭФЁ";

    private String encoders = RU_ENCODERS;
    private String frequency = RU_FREQUENCY;
    private Statistics statistics = new Statistics();

    public CharsetDetector() {
    }

    public CharsetDetector(String encoders, String frequency)
            throws UnsupportedEncodingException  {
        for(String encoder : encoders.split(",")) {// check encoder exists
           String s = new String(RU_FREQUENCY.getBytes(), encoder); 
        }
        this.encoders = encoders;
        this.frequency = frequency;
    }

    public String[] listEncoders() {
        return encoders.split(",");
    }
    
    public String getFrequency() {
        return frequency;
    }

    public String detect(File file) throws FileNotFoundException, IOException {
        return detect(new FileInputStream(file));
    }

    public String detect(byte[] buf, int buflen) throws UnsupportedEncodingException {
        String charset = null;
        int weight = 0;
        statistics = new Statistics();
        for (String charsetName : listEncoders()) {
            String sbuf = new String(buf, 0, buflen, charsetName);
            int wset = lettersFrequency(sbuf);
            statistics.addEntry(charsetName, wset, lettersCount(sbuf), sbuf.length());
            if (weight < wset) {
                charset = charsetName;
                weight = wset;
            }
        }
        return charset;
    }

    public String detect(InputStream in) throws IOException {
        byte[] buf = new byte[512];
        int bufLen = in.read(buf);
        return detect(buf, bufLen);
    }

    int lettersFrequency(String s) {
        int frequency = 0;
        for (char c : s.toCharArray()) {
            int f = this.frequency.indexOf(c);
            frequency += (f == -1 ? 0 : this.frequency.length() - f);
        }
        return frequency;
    }

    int lettersCount(String s) {
        int count = 0;
        for (char c : s.toLowerCase().toCharArray()) {
            if (this.frequency.indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
    }

    public Statistics statistics() {
        return statistics;
    }

}
