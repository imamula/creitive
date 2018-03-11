package ivan.mamula.creitive.Utils.Network;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ivan.mamula.creitive.Utils.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ivan Mamula on 3/9/18.
 * mamula82@gmail.com
 * Skype id: mamula82
 * +381(0)642035511
 */
public class RetrofitClient {
    private static Retrofit retrofit = null;


    private static Retrofit makeClient(final Context context) {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain)
                        throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header(Constants.ACCEPT_HEADER_NAME,
                                    Constants.ACCEPT_HEADER_VALUE)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            })
                    .readTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
                    .connectTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS);
            OkHttpClient client = httpClient.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static RESTAPI getRetrofitClient(Context context) {
        return makeClient(context).create(RESTAPI.class);
    }

}
