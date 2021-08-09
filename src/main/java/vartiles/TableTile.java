package vartiles;

import java.util.ArrayList;
import java.util.Map;

public class TableTile extends DefaultTile {
    private final String[][] table;

    public TableTile(String[][] table) {
        this.table = table;
    }

    public TableTile(Map<?, ?> map) {
        this(map.entrySet().stream()
                .map(e -> new String[]{e.getKey().toString(), e.getValue().toString()})
                .toArray(String[][]::new));
    }

    public TableTile(ArrayList<?> list) {
        this(list.stream()
                .map(e -> new String[]{e.toString()})
                .toArray(String[][]::new));
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