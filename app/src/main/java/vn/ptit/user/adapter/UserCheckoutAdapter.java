package vn.ptit.user.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Cart;
import vn.ptit.model.LineItem;
import vn.ptit.user.UserCartActivity;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;

public class UserCheckoutAdapter extends RecyclerView.Adapter<UserCheckoutAdapter.UserCheckoutViewHolder>{
    private List<LineItem> mList;
    private Context context;

    public UserCheckoutAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.context = context;
    }

    public void setList(List<LineItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public UserCheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_checkout, parent, false);
        return new UserCheckoutAdapter.UserCheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCheckoutViewHolder holder, int position) {
        LineItem lineItem = mList.get(position);
        holder.tv1.setText(lineItem.getLaptop().getName());
        holder.tv2.setText(MoneyFormat.format(lineItem.getLaptop().getPrice()*lineItem.getQuantity()*(100-lineItem.getLaptop().getDiscount())/100) + " đ");
        holder.tv3.setText("x"+lineItem.getQuantity()+"");
        Glide.with(context)
                .load(lineItem.getLaptop().getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserCheckoutViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2, tv3;
        private ImageView img;

        public UserCheckoutViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.nameCartItemLaptop);
            tv2 = view.findViewById(R.id.priceCartItemLaptop);
            tv3 = view.findViewById(R.id.quantity);
            img = view.findViewById(R.id.imgCartItemLaptop);
        }
    }


}
