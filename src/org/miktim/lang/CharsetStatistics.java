/*
 * Charset Statistics, MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CharsetStatistics {

    public class Entry {

        public String charset;
        public boolean detected = false;
        public long rating;
        public long lettersCount;
        public int textLength;

        @Override
        public String toString() {
            return String.format("%-17s\t%d\t%d\t%d\t%d%%",
                    (detected ? "* " : "  ") + charset, rating, lettersCount, textLength, lettersCount * 100 / textLength);
        }
    }

    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    void addEntry(String cs, int rt, int lc, int tl) {
        Entry entry = new Entry();
        entry.charset = cs;
        entry.rating = rt;
        entry.lettersCount = lc;
        entry.textLength = tl;
        entries.put(cs, entry);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        List<String> entriesList = new ArrayList<>(entries.keySet());
        for (String charset : entriesList) {
            Entry entry = entries.get(charset);
            sb.append(String.format("%s\r\n", entry));
        }
        return sb.toString();
    }

}
