package ivan.mamula.creitive.Network;

import android.content.Context;

import ivan.mamula.creitive.Utils.Constants;
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
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RESTAPI getRetrofitClient(Context context) {
        return makeClient(context).create(RESTAPI.class);
    }

}
