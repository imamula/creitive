package ivan.mamula.creitive.Network;

import ivan.mamula.creitive.Network.Models.LoginRequestBody;
import ivan.mamula.creitive.Network.Models.LoginResponseModel;
import ivan.mamula.creitive.Utils.Constants;
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
            Constants.ACCEPT_HEADER
    })

    @POST("/login")
    Call<LoginResponseModel> login(@Body LoginRequestBody body,
                                   @Header(Constants.LOGIN_CONTENT_TYPE_HEADER_NAME)
                                           String contentRange);
}
