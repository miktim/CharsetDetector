/*
 * Charset Detector Statistics, MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.lang;

import java.util.ArrayList;

public class Statistics {

    public class Entry {

        public String encoder;
        public long weight;
        public long lettersCount;
        public int textLength;

        @Override
        public String toString() {
            return String.format("%-15s\t%d\t%d\t%d\t%d%%",
                    encoder, weight, lettersCount, textLength, lettersCount*100/textLength);
        }
    }

    public ArrayList<Entry> entries = new ArrayList<>();

    void addEntry(String en, int wg, int lc, int tl) {
        Entry entry = new Entry();
        entry.encoder = en;
        entry.weight = wg;
        entry.lettersCount = lc;
        entry.textLength = tl;
        entries.add(entry);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Entry entry : entries) {
            sb.append(String.format("%s\r\n", entry));
        }
        return sb.toString();
    }

}
