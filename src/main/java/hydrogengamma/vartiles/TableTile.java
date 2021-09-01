package hydrogengamma.vartiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class TableTile extends DefaultTile {
    private final String[][] table;

    public TableTile(String[][] table, String label) {
        super(label);
        this.table = table;
    }

    public TableTile(Map<?, ?> map, String label) {
        this(map.entrySet().stream()
                .map(e -> new String[]{e.getKey().toString(), e.getValue().toString()})
                .sorted((l, r) -> {
                    if (l[0].equals(r[0])) {
                        return l[1].compareTo(r[1]);
                    }
                    return l[0].compareTo(r[0]);
                })
                .toArray(String[][]::new), label);
    }

    public TableTile(ArrayList<?> list, String label) {
        this(list.stream()
                .map(e -> new String[]{e.toString()})
                .sorted(Comparator.comparing(l -> l[0]))
                .toArray(String[][]::new), label);
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder();
        content.append("<table class=\"table table-bordered w-auto table-non-fluid\"><tbody>");
        for (String[] strings : table) {
            content.append("<tr>");
            for (String string : strings) {
                content.append("<td>" + string + "</td>");
            }
            content.append("</tr>");
        }
        content.append("</tbody></table>");
        return content.toString();
    }
}