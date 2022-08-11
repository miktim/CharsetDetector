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
    static final String RU_EXPECTED = "оеаитнсрвлкмдпуяыгзбчйчъжьюшцщэфёОЕАИТНСРВЛКМДПУЯЫГЗБЧЙЧЪЖЬЮШЦЩЭФЁ";
//    static final String EN_EXPECTED = "eariotnslcudpmhgbfywkvxzjqEARIOTNSLCUDPMHGBFYWKVXZJQ";
    static String UNWANTED = "";
//    static final String PSEUDOGRAPHICS = "─│┌┐└┘├┤┬┴┼▀▄█▌▐░▒▓⌠■═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬";

    private String encoders = RU_ENCODERS;
    private String expected = RU_EXPECTED;//+ EN_EXPECTED;
    private String unwanted = UNWANTED;
    private CharsetStatistics statistics = new CharsetStatistics();

    static {
        byte[] unwanted = new byte[31];
        for (int i = 0; i < unwanted.length; i++) {
            unwanted[i] = (byte) i;
        }
        UNWANTED = (new String(unwanted)).replaceAll("\t|\r|\n|\f|\b| ", "") + (char) 0x7F;
    }

    public CharsetDetector() {
    }

    public CharsetDetector(String encoders, String expected, String unwanted)
            throws UnsupportedEncodingException {
        this.encoders = encoders.replace(" ", "");
        for (String encoder : encoders.split(",")) {// check encoder exists
            String s = new String(RU_ENCODERS.getBytes(), encoder);
        }
        this.expected = expected;
        if (unwanted != null) {
            this.unwanted = unwanted;
        }
    }

    public String[] listEncoders() {
        return encoders.split(",");
    }

    public String getExpected() {
        return expected;
    }

    public String getUnwanted() {
        return unwanted;
    }

    public String detect(File file) throws FileNotFoundException, IOException {
        return detect(new FileInputStream(file));
    }

    public String detect(InputStream in) throws IOException {
        byte[] buf = new byte[512];
        int bufLen = in.read(buf);
        return detect(buf, bufLen);
    }

    public String detect(byte[] buf, int bytesIn) throws UnsupportedEncodingException {
        String charset = null;
        int rating = 0;
        statistics = new CharsetStatistics();
        for (String charsetName : listEncoders()) {
            String sbuf = new String(buf, 0, bytesIn, charsetName);
            int csRating = charsetRating(sbuf);
            statistics.addEntry(charsetName, csRating, lettersCount(sbuf), sbuf.length());
            if (rating < csRating) {
                charset = charsetName;
                rating = csRating;
            }
        }
        if (charset != null) {
            statistics.entries.get(charset).detected = true;
        }
        return charset;
    }

    public CharsetStatistics getStatistics() {
        return statistics;
    }

    int charsetRating(String s) {
        int expectedCnt = 0;
        int unwantedCnt = 0;
        int base = Math.max(this.expected.length(), this.unwanted.length());
        for (char c : s.toCharArray()) {
            int f = this.expected.indexOf(c);
            expectedCnt += (f == -1 ? 0 : base - f);
            f = this.unwanted.indexOf(c);
            unwantedCnt += (f == -1 ? 0 : base - f);
        }

        return expectedCnt - unwantedCnt;
    }

    int lettersCount(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (this.expected.indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
    }
}
