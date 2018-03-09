package ivan.mamula.creitive.Network;

import ivan.mamula.creitive.Network.Models.LoginRequestBody;
import ivan.mamula.creitive.Network.Models.LoginResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Ivan Mamula on 3/9/18.
 * mamula82@gmail.com
 * Skype id: mamula82
 * +381(0)642035511
 */
public interface RESTAPI {
    @Headers({
            "Accept: application/json"
    })

    @POST("/login")
    Call<LoginResponseModel> login(@Body LoginRequestBody body,
                                   @Header("Content-Type") String contentRange);
}
