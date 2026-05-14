     package ae.okhaz.boss.Utils;

/**
 * Created by Avinash on 08,September,2020
 */
public class ConstactsUrls {


//    public static String BASE_URL = "http://hkmaryam.in/THEYARDGroceryAPI/";
//    public static String IMG_URL = "http://hkmaryam.in/THEYARDGroceryAPI/";

    public static String BASE_URL = "https://okhaz.ae/OkhazMobileApp";

    public static final String API_BASE_URL = BASE_URL+"/v1/";
    public static final String IMAGES_BASE_URL = BASE_URL+"/";
    public static final String UPLOADED_BASE_URL = BASE_URL+"upload/userProfile/";
    public static final String USER_PROFILE_PICTURE = BASE_URL +"upload/";
    public static final int CONNECTION_TIMEOUT = 5; // 10 seconds
    public static final int READ_TIMEOUT = 3; // 2 seconds
    public static final int WRITE_TIMEOUT = 3; // 2 seconds

    public static final int RECIPE_REFRESH_TIME = 60 * 60 * 24 * 30; // 30 days (in seconds)
}
