package hydrogengamma.vartiles;

import java.util.List;

public class SingleColumnTableTile extends DefaultTile {
    private final List<String> table;

    public SingleColumnTableTile(List<String> table, String label) {
        super(label);
        this.table = table;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder();
        content.append("<table class=\"table table-bordered w-auto table-non-fluid\"><tbody>");
        for (String row : table) {
            content.append(String.format("<tr><td>%s</td></tr>", row));
        }
        content.append("</tbody></table>");
        return content.toString();
    }
}