package hydrogengamma.vartiles;

import hydrogengamma.utils.Pair;

import java.util.List;

public class DoubleColumnTableTile extends DefaultTile {
    private final List<Pair<String, String>> table;

    public DoubleColumnTableTile(List<Pair<String, String>> table, String label) {
        super(label);
        this.table = table;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder();
        content.append("<table class=\"table table-bordered w-auto table-non-fluid\"><tbody>");
        for (Pair<String, String> row : table) {
            content.append(String.format("<tr><td class=\"table-success\">%s</td><td>%s</td></tr>", row.first, row.second));
        }
        content.append("</tbody></table>");
        return content.toString();
    }
}