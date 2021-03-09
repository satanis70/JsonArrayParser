package ermilov.jsonarrayparser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ermilov.jsonarrayparser.Api.JsonApi;
import ermilov.jsonarrayparser.holder.Holder;
import ermilov.jsonarrayparser.holder.OnLoadMoreListener;
import ermilov.jsonarrayparser.model.JsonModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Holder holder;
    ArrayList<String> imageList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_images);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.npoint.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        JsonApi jsonApi = retrofit.create(JsonApi.class);

        jsonApi.getImages()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonModel jsonModel) {
                        imageList.addAll(jsonModel.getImages());
                        Log.i("tag", jsonModel.getImages().toString());
                        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        holder = new Holder(imageList, getApplicationContext(), recyclerView);
                        holder.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                if (imageList.size()<=20){
                                    imageList.add(null);
                                    holder.notifyItemInserted(imageList.size()-1);
                                    imageList.remove(imageList.size()-1);
                                    holder.notifyItemRemoved(imageList.size());
                                    int start = imageList.size();
                                    int end = start + 20;
                                    for(int i = start+1; i <= end; i++){
                                        imageList.addAll(jsonModel.getImages());
                                    }
                                    holder.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MainActivity.this, "Загружено!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("tag", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                        recyclerView.setAdapter(holder);
                    }
                });
    }
}