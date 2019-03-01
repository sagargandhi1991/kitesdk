package ly.kite.cliqueshare;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ly.kite.R;

public class CliqueshareListAdapter extends RecyclerView.Adapter<CliqueshareListAdapter.ViewHolder> {
    private List<BoardImageModel> values;
    BoardImageModel model;
    Context ctx;
    boolean isMultiSelected;
    private int lastSelectedPosition = -1;
    // Provide a suitable constructor (depends on the kind of dataset)
    public CliqueshareListAdapter(List<BoardImageModel> myDataset,Context context,boolean multiselect) {
        values = myDataset;
        this.ctx=context;
        this.isMultiSelected=multiselect;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView boardImage;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            boardImage = (ImageView) v.findViewById(R.id.boarditem);

        }
    }

//    public void add(int position, String item) {
//        values.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(int position) {
//        values.remove(position);
//        notifyItemRemoved(position);
//    }



    // Create new views (invoked by the layout manager)
    @Override
    public CliqueshareListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.cliqueshare_board_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        model=values.get(position);
        Picasso.with(ctx )
                .load( model.getImageUrl() )
                .fit()
                .centerCrop()
                .into(holder.boardImage);

        holder.layout.setBackgroundColor(model.isSelected() ? Color.BLACK : Color.WHITE);

        holder.boardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isMultiSelected) {
                    model.setSelected(!model.isSelected());
                    holder.layout.setBackgroundColor(model.isSelected() ? Color.BLACK : Color.WHITE);
                }else{
                    if(lastSelectedPosition > 0) {
                        values.get(lastSelectedPosition).setSelected(false);
                    }

                    model.setSelected(!model.isSelected());
                    holder.layout.setBackgroundColor(model.isSelected() ? Color.BLACK : Color.WHITE);

                    // store last selected item position
                    lastSelectedPosition = holder.getAdapterPosition();
                }
            }
        });

        //final String name = values.get(position);
       // holder.boardImage.setText(name);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}