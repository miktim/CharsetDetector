/*
 * Charset Detector Test MIT (c) 2022 miktim@mail.ru
 */

//package lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.miktim.lang.CharsetDetector;

public class DetectorTest {

    static void log(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String fileNames
        ="koi8-r.txt,cp1251.txt,cp866.txt,utf-16.txt,utf-16-bom.txt,utf-8.txt,utf-8-bom.txt";

        CharsetDetector charsetDetector = new CharsetDetector();
        for (String fileName : fileNames.split(",")) {
            File file = new File(".", fileName);
            String charset = charsetDetector.detect(new FileInputStream(file));
            log(fileName + " " + charset);
            log ("Statistics:");
            log(charsetDetector.statistics().toString());
            if (charset == null) {
                continue;
            }
            Reader rdr = new InputStreamReader(new FileInputStream(file), charset);
            char[] cbuf = new char[512];
            int cbufLen = rdr.read(cbuf);
            rdr.close();
            log(new String(cbuf));
        }
    }
}
