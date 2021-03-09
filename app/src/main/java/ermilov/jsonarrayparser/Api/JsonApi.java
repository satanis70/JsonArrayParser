package ermilov.jsonarrayparser.Api;

import java.util.List;

import ermilov.jsonarrayparser.model.JsonModel;
import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonApi {
    @GET("dad63dd6b6d94d5b21c1")
    Flowable<JsonModel> getImages();
}
