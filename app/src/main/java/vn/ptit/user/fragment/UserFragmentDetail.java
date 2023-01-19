package vn.ptit.user.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import vn.ptit.R;
import vn.ptit.admin.AdminAddLaptop;
import vn.ptit.model.Comment;
import vn.ptit.model.Laptop;
import vn.ptit.model.User;
import vn.ptit.user.UserDetailLaptopActivity;
import vn.ptit.user.adapter.CommentAdapter;
import vn.ptit.user.adapter.UserSameItemAdapter;
import vn.ptit.util.Encoding;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserFragmentDetail extends Fragment {
    private ImageView img;
    private TextView tvName, tvPrice, tvDiscount, tvPriceCur, tvCpu, tvHardDrive, tvRam, tvVga, tvScreen, tvManufacturer;
    private RecyclerView recyclerViewSameItem, recyclerViewComment;
    private UserSameItemAdapter userSameItemAdapter;
    private CommentAdapter commentAdapter;
    private RatingBar ratingBar1, rbComment;
    private Button btnComment;
    private EditText etContent;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratingBar1 = view.findViewById(R.id.rbStar);
        rbComment = view.findViewById(R.id.rbComment);
        img = view.findViewById(R.id.imgLaptopDetail);
        tvName = view.findViewById(R.id.nameLaptopDetail);
        tvPrice = view.findViewById(R.id.priceLaptopDetail);
        tvDiscount = view.findViewById(R.id.discountLaptopDetail);
        tvPriceCur = view.findViewById(R.id.priceCurLaptopDetail);
        tvCpu = view.findViewById(R.id.cpuLaptopDetail);
        tvHardDrive = view.findViewById(R.id.hardDriveLaptopDetail);
        tvRam = view.findViewById(R.id.ramLaptopDetail);
        tvVga = view.findViewById(R.id.vgaLaptopDetail);
        tvScreen = view.findViewById(R.id.screenLaptopDetail);
        tvManufacturer = view.findViewById(R.id.manufacturerLaptopDetail);
        btnComment = view.findViewById(R.id.btnComment);
        etContent = view.findViewById(R.id.etComment);

        Intent intent = getActivity().getIntent();
        Laptop laptop = (Laptop) intent.getSerializableExtra("laptop");

        tvName.setText(laptop.getName());
        tvCpu.setText(laptop.getCpu());
        tvRam.setText(laptop.getRam());
        tvHardDrive.setText(laptop.getHardDrive());
        tvVga.setText(laptop.getVga());
        if (laptop.getDiscount() > 0) {
            tvPrice.setText(MoneyFormat.format(laptop.getPrice()) + " đ");
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setText("Bạn tiết kiệm được " + laptop.getDiscount() + "%");
        } else {
            tvPrice.setText("");
            tvDiscount.setText("");
        }
        tvScreen.setText(laptop.getScreen() + "");
        tvManufacturer.setText(laptop.getManufacturer().getName());
        tvPriceCur.setText(MoneyFormat.format(laptop.getPrice() * (100 - laptop.getDiscount()) / 100) + " đ");

        Glide.with(this)
                .load(laptop.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(img);

        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.videoPreview);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = laptop.getVideo();
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        recyclerViewSameItem = view.findViewById(R.id.rvUserSameItem);
        userSameItemAdapter = new UserSameItemAdapter(getContext(), laptop.getManufacturer().getId(), laptop.getId());
        userSameItemAdapter.setLaptopClickListener(new UserSameItemAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userSameItemAdapter.getLaptop(position);
                Intent intent = new Intent(getContext(), UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewSameItem.setLayoutManager(manager);
        recyclerViewSameItem.setAdapter(userSameItemAdapter);

        ratingBar1.setRating(laptop.getAverageStar());

        recyclerViewComment = view.findViewById(R.id.rvComment);
        commentAdapter = new CommentAdapter(getContext(), laptop.getComments());
        LinearLayoutManager managerComment = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewComment.setLayoutManager(managerComment);
        recyclerViewComment.setAdapter(commentAdapter);

        sharedPreferences = getActivity().getSharedPreferences("dataApp", MODE_PRIVATE);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser(Integer.parseInt(laptop.getId()));
            }
        });

    }

    private void postData(Comment comment, ProgressDialog pDialog) {

        String url = MyDomainService.name + "/api/laptop/insert-comment";

        JSONObject obj = null;
        try {
            obj = new JSONObject(new Gson().toJson(comment));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        System.out.println(obj);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();

                        Laptop laptop = new Gson().fromJson(String.valueOf(response), Laptop.class);
                        commentAdapter.setmList(laptop.getComments());
                        System.out.println(laptop.getAverageStar());
                        ratingBar1.setRating(laptop.getAverageStar());
                        etContent.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void getUser(int laptopId) {
        if(etContent.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Bạn hãy nhập đánh giá",Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/user/find-by-username/" + username;

        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = new Gson().fromJson(Encoding.fixEncoding(response), User.class);

                float rating = rbComment.getRating();
                String content = etContent.getText().toString();

                Comment comment = new Comment();
                comment.setStar((int) rating);
                comment.setContent(content);
                comment.setUser(user);
                comment.setLaptopId(laptopId);

                postData(comment, pDialog);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
