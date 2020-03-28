package utils;

public class Constants {

    private static final String ROOT_URL = "http://192.168.64.2/test_json/Android/v1/";

    public static final String URL_REGISTER = ROOT_URL + "registerUser.php";
    public static final String URL_LOGIN = ROOT_URL + "userLogin.php";
    public static final String URL_SHOWDATA = ROOT_URL + "showdata.php";

    // login
    public static final String SHARED_PREFERENCES_NAME = "login_portal";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "first_name";
    public static final String CONTACT_NO = "contact";
    public static final String EMAIL_ID = "email_id";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
}
