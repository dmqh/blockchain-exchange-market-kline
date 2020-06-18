package mohen.exchange.symbol;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 币对解析
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public class SymbolParse {

    private static Map<String, Set<String>> map = new HashMap<>();

    public static Set<String> get(String flag) throws IOException {
        if (map.get(flag) == null) {
            SpotSymbols spotSymbols = new SpotSymbols();
            spotSymbols.syncGet();
        }
        return map.get(flag);
    }

    protected static void put(String key, String value) {
        Set<String> set = map.get(key);
        if (set == null) {
            synchronized (map) {
                set = new HashSet<>();
                set.add(value);
                map.put(key, set);
            }
        } else {
            set.add(value);
        }
    }
}

