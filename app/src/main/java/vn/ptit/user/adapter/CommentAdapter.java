package vn.ptit.user.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditLaptop;
import vn.ptit.model.Comment;
import vn.ptit.model.Laptop;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> mList;
    private Context context;

    public CommentAdapter(Context context,List<Comment> mList) {
        this.mList = mList;
        this.context = context;
    }

    public void setmList(List<Comment> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mList.get(position);
        holder.tv1.setText(comment.getUser().getFullName());
        holder.tv2.setText(comment.getContent());

        Glide.with(context)
                .load(comment.getUser().getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.img);

        holder.starComment.setRating(comment.getStar());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2;
        private ImageView img;
        private RatingBar starComment;

        public CommentViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.tvNameComment);
            tv2 = view.findViewById(R.id.tvContentComment);
            img = view.findViewById(R.id.civ1);
            starComment = view.findViewById(R.id.starComment);
        }
    }


}
