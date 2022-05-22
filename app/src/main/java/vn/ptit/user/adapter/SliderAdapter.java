package vn.ptit.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import vn.ptit.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderHolder> {
    private int[] images;

    public SliderAdapter() {
        images = new int[]{R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};
    }

    @Override
    public SliderHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new SliderHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderHolder viewHolder, int position) {
        viewHolder.iv.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class SliderHolder extends ViewHolder {
        private ImageView iv;

        public SliderHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.itemSlider);

        }
    }
}
