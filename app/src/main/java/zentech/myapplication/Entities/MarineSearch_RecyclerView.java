package zentech.myapplication.Entities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

import zentech.myapplication.ClientSide.BoatManagementActivity;
import zentech.myapplication.ClientSide.MarineSearchItemListActivity;
import zentech.myapplication.Entities.Singletons.ImageLoader;
import zentech.myapplication.Entities.Singletons.MyApplication;
import zentech.myapplication.R;


/**
 * Created by izik on 12/26/2016.
 */

public class MarineSearch_RecyclerView extends RecyclerView.Adapter<MarineSearch_RecyclerView.ViewHolder>
{
    private final ArrayList<Marina> mValues;
    private boolean mTwoPane;
    private Boat selectedBoat;
    private Activity activity;
    private String[] data;

    public MarineSearch_RecyclerView(ArrayList<Marina> marinas)
    {
        mValues = marinas;
    }

    public MarineSearch_RecyclerView(MarineSearchItemListActivity marineSearchItemListActivity, String[] marinesPicturesPaths, ArrayList<Marina> marinas, boolean mTwoPane, Boat selectedBoat)
    {
        activity = marineSearchItemListActivity;
        data = marinesPicturesPaths;
        mValues = marinas;
        this.mTwoPane = mTwoPane;
        this.selectedBoat = selectedBoat;
    }

    @Override
    public MarineSearch_RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MarineSearch_RecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MarineSearch_RecyclerView.ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);

        //set image
        holder.displayImage(data[position]);

        //this is like onItemClickListener
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (mTwoPane)
                {
                    //TODO: tablet view
                    /*
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MarineDetailFragment.ARG_ITEM_ID, holder.mItem);
                    MarineDetailFragment fragment = new MarineDetailFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                    */
                }
                else
                {
                    Context context = v.getContext();
                    //Intent intent = new Intent(context, MarineDetailActivity.class);
                    //intent.putExtra(MarineDetailFragment.ARG_ITEM_ID, holder.mItem);
                    //intent.putExtra(BoatManagementActivity.BOAT_KEY,selectedBoat);
                    //context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final ImageView mMarineImageView;
        public Marina mItem;
        private ImageLoader imageLoader;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mMarineImageView = (ImageView) view.findViewById(R.id.marineImage);
            imageLoader = ((MyApplication)activity.getApplication()).getImageLoader();//  new ImageLoader(activity.getApplicationContext());
        }

        public void displayImage(String picPath)
        {
            imageLoader.DisplayImage(picPath, mMarineImageView);
        }

        @Override
        public String toString()
        {
            if(mItem != null)
            {
                return super.toString() + " '" + mItem.getName() + "'";
            }

            return "";
        }
    }
}
