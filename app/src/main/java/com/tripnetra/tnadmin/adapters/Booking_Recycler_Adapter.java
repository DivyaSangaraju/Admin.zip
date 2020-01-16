package com.tripnetra.tnadmin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.bookings.CarBookVoucherAct;
import com.tripnetra.tnadmin.bookings.HotelBookVoucherAct;
import com.tripnetra.tnadmin.bookings.TourBookVoucherAct;

import java.util.ArrayList;
import java.util.List;

public class Booking_Recycler_Adapter extends RecyclerView.Adapter<Booking_Recycler_Adapter.ViewHolder> implements Filterable {

    private List<DataAdapter> listadapter;
    private List<DataAdapter> arrayadap;
    private Context con;

    public Booking_Recycler_Adapter(List<DataAdapter> listAdapter,Context c) {
        super();
        this.listadapter = listAdapter;
        this.arrayadap = listAdapter;
        this.con=c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_booking_rep, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView HNameTv,GnameTv,MobNoTv,PnrNoTv,BookStatusTV, RoomTypeTV,CinTV,CoutTv,AcTv;
        private Context context;
        ViewHolder(View itemView) {

            super(itemView);

            HNameTv = itemView.findViewById(R.id.HNameTV);
            GnameTv = itemView.findViewById(R.id.nameTv);
            PnrNoTv = itemView.findViewById(R.id.PnrTv);
            MobNoTv = itemView.findViewById(R.id.mobileTv);
            CoutTv = itemView.findViewById(R.id.COutTv);
            BookStatusTV = itemView.findViewById(R.id.PnrStatTv);
            RoomTypeTV = itemView.findViewById(R.id.RNameTV);
            CinTV = itemView.findViewById(R.id.CInTv);
            AcTv = itemView.findViewById(R.id.AdcTv);
            context = itemView.getContext();
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            String type = listadapter.get(getAdapterPosition()).getType();
            Intent intent = new Intent();

            switch (type) {
                case "hotel":
                    intent = new Intent(context, HotelBookVoucherAct.class);
                    break;
                case "car":
                    intent = new Intent(context, CarBookVoucherAct.class);
                    break;
                case "tour": case "darshan":
                    intent = new Intent(context, TourBookVoucherAct.class);
                    intent.putExtra("type", type);
                    break;
            }
            intent.putExtra("pnrnumber", listadapter.get(getAdapterPosition()).getHid());
            intent.putExtra("sid", listadapter.get(getAdapterPosition()).getCap2());
            context.startActivity(intent);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int pos) {

        DataAdapter dataAdap = listadapter.get(pos);
        vh.HNameTv.setText(dataAdap.getHname());
        vh.GnameTv.setText(dataAdap.getGname());
        vh.PnrNoTv.setText(dataAdap.getHid());
        vh.MobNoTv.setText("  "+dataAdap.getMobile());
        vh.BookStatusTV.setText(dataAdap.getStatus());
        vh.RoomTypeTV.setText(dataAdap.getRName());
        vh.CinTV.setText(dataAdap.getInDate());
        vh.CoutTv.setText(dataAdap.getOutDate());
        vh.AcTv.setText(dataAdap.getNewName());



        String  Tommorow=String.valueOf(android.text.format.DateFormat.format
                ("yyyy-MM-dd", System.currentTimeMillis()+86400000));
        if(dataAdap.getInDate().compareTo(Tommorow) == 0 && dataAdap.getStatus().equals("CONFIRM")){
            vh.CinTV.setTextColor(Color.parseColor("#ffffff"));
            vh.CinTV.setAnimation(AnimationUtils.loadAnimation(con,R.anim.flash_leave_now));
            vh.CinTV.setTextSize(25);
            vh.CinTV.setBackgroundResource(R.drawable.box_grey1);

        }
        else {
            vh.CinTV.setTextColor(Color.parseColor("#9C9A9C"));
            vh.CinTV.setTextSize(20);
            vh.CinTV.setBackgroundResource(R.drawable.box_grey2);
        }




        String ss = dataAdap.getStatus().toLowerCase();
        if(ss.contains("confirm")){
            vh.BookStatusTV.setTextColor(Color.parseColor("#049C72"));
        }else if(ss.contains("cancel")){
            vh.BookStatusTV.setTextColor(Color.parseColor("#CB0909"));
        }else if(ss.contains("process")){
            vh.BookStatusTV.setTextColor(Color.GRAY);
        }

        if(dataAdap.getType().equals("tour") || dataAdap.getType().equals("car")){
            vh.CinTV.setVisibility(View.GONE);
        } else{
            vh.CinTV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {return listadapter.size();}

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSeq) {
                String charString = charSeq.toString();
                if (charString.isEmpty()) {
                    listadapter = arrayadap;
                } else {
                    ArrayList<DataAdapter> filteredList = new ArrayList<>();
                    for (DataAdapter filterdata : arrayadap) {
                        if (filterdata.getGname().toLowerCase().contains(charString) || filterdata.getHid().toLowerCase().contains(charString) ||
                                filterdata.getHname().toLowerCase().contains(charString)) {
                            filteredList.add(filterdata);
                        }
                    }
                    listadapter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listadapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listadapter = (ArrayList<DataAdapter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}