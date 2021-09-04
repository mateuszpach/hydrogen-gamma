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