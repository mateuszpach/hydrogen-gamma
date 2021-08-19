package vartiles;


public class InfoTile extends DefaultTile {
    private String info;

    public InfoTile(String info, String label) {
        this.info = info;
        this.label = label;
    }

    public InfoTile(String info) {
        this(info, null);
    }

    @Override
    public String getContent() {
        return "$\\text{" + info + "}$";
    }
}
