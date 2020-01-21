package core.constants;

public final class Endpoints {
    public static final String PATH_TO_CREDITS_FILE = "src/test/resources/tokens.properties";
    public static final String PATH_TO_URL_FILE = "src/test/resources/url.properties";
    public static final String BASE_URL = "https://api.trello.com";
    public static final String BASE_PATH = "/1";

    public static final String LISTS = "/lists";
    public static final String LIST = "/lists/{id}";
    public static final String CARDS = "/cards";
    public static final String CARD = "/cards/{id}";
    public static final String BOARD_ID = "/members/me?fields=idBoards";
    public static final String BOARD = "/boards/{id}";
    public static final String BOARDS = "/boards/";

}
