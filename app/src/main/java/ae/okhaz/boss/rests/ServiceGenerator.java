package ae.okhaz.boss.rests;



import ae.okhaz.boss.Utils.ConstactsUrls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient client = new OkHttpClient.Builder()

            // establish connection to server
            .connectTimeout(ConstactsUrls.CONNECTION_TIMEOUT, TimeUnit.MINUTES)

            .addInterceptor(logging)
            // time between each byte read from the server
            .readTimeout(ConstactsUrls.READ_TIMEOUT, TimeUnit.MINUTES)

            // time between each byte sent to server
            .writeTimeout(ConstactsUrls.WRITE_TIMEOUT, TimeUnit.MINUTES)

            .retryOnConnectionFailure(true)

            .build();


    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(ConstactsUrls.API_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static ApiInterface delivery = retrofit.create(ApiInterface.class);

    public static ApiInterface getDelivery() {
        return delivery;
    }
}
