package hydrogengamma.vartiles;


public class InfoTile extends DefaultTile {
    private final String info;

    public InfoTile(String info, String label) {
        super(label);
        this.info = info;
    }

    @Override
    public String getContent() {
        return "$\\text{" + info + "}$";
    }
}
