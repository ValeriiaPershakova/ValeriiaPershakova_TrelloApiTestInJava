package core.constants;

public enum Filters {
    OPEN("open"),
    ALL("all"),
    NONE("none"),
    CLOSED("closed");

    private String param;

    Filters(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
