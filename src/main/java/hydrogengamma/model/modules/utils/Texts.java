package hydrogengamma.model.modules.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public interface Texts {
    static String[][] convertMapTo2dArray(Map<?, ?> map) {
        return map.entrySet().stream()
                .map(e -> new String[]{e.getKey().toString(), e.getValue().toString()})
                .sorted((l, r) -> {
                    if (l[0].equals(r[0])) {
                        return l[1].compareTo(r[1]);
                    }
                    return l[0].compareTo(r[0]);
                })
                .toArray(String[][]::new);
    }

    static String[][] convertArrayListTo2dArray(ArrayList<?> list) {
        return list.stream()
                .map(e -> new String[]{e.toString()})
                .sorted(Comparator.comparing(l -> l[0]))
                .toArray(String[][]::new);
    }
}
