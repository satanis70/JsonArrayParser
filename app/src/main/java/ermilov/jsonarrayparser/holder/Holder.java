package ermilov.jsonarrayparser.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ermilov.jsonarrayparser.R;
import ermilov.jsonarrayparser.model.JsonModel;

public class Holder extends RecyclerView.Adapter<Holder.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<String> imageList;
    Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;



    public Holder(List<String> imageList, Context context, RecyclerView recyclerView) {
        this.imageList = imageList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount<=(lastVisibleItem+visibleThreshold)){
                    if (onLoadMoreListener!=null){
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return imageList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public Holder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_recycler, parent, false);
            return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context)
                    .load(imageList.get(position))
                    .apply(new RequestOptions().override(600, 600))
                    .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }
    }




}
