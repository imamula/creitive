package ivan.mamula.creitive.Network;

import java.util.List;

import ivan.mamula.creitive.Network.Models.BlogListItem;
import ivan.mamula.creitive.Network.Models.LoginRequestBody;
import ivan.mamula.creitive.Network.Models.LoginResponseModel;
import ivan.mamula.creitive.Utils.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    @Headers({
            Constants.ACCEPT_HEADER
    })
    @GET("/blogs")
    Call<List<BlogListItem>> getBlogs(@Header(Constants.X_AUTHORIZE_HEADER_NAME) String token);
}
