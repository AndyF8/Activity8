package com.example.activity8.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.activity8.EditTeman;
import com.example.activity8.MainActivity;
import com.example.activity8.R;
import com.example.activity8.app.AppController;
import com.example.activity8.database.Teman;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {

    private ArrayList<Teman> listData;


    public TemanAdapter(ArrayList<Teman> listData) {
        this.listData = listData;

    }



    @Override
    public TemanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        View view = layoutInf.inflate(R.layout.row_data_teman,parent, false);
        return new TemanViewHolder(view);


    }

    @Override
    public void onBindViewHolder(TemanViewHolder holder, int position) {

        String id, nm,tlp;

        id = listData.get(position).getId();
        nm = listData.get(position).getNama();
        tlp = listData.get(position).getTelpon();

        holder.namaTxt.setText(nm);
        holder.namaTxt.setTextColor(Color.BLUE);
        holder.namaTxt.setTextSize(20);
        holder.telponTxt.setText(tlp);

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View w) {
                PopupMenu  pm = new PopupMenu(w.getContext(), w);

                pm. inflate(R.menu.poup1);

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.edit:
                                Bundle bendel = new Bundle();

                                bendel.putString("kunci1", id);
                                bendel.putString("kunci2", nm);
                                bendel.putString("kunci3", tlp);

                                Intent x = new Intent(w.getContext(), EditTeman.class);
                                x.putExtras(bendel);
                                w.getContext().startActivity(x);

                                break;

                            case R.id.hapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(w.getContext());
                                alertdb.setTitle("Yakin " +nm+ "akan dihapus ?");
                                alertdb.setMessage("Tekan ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HapusData(id);
                                        Toast.makeText(w.getContext(), "Data" +id+ "telah dihapus", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(w.getContext(), MainActivity.class);
                                        w.getContext().startActivity(intent);


                                    }
                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();


                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();
                                break;

                        }
                        return true;
                    }
                });
                pm.show();
               return true;

            }
        });


    }

    private void HapusData(final String idx) {
        String url_update ="https://20200140079.praktikumtiumy.com/deletetm.php";
        final  String TAG = MainActivity.class.getSimpleName();
        final  String TAG_SUCCES = "succes";
        final int [] sukses = new int [1];
        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_SUCCES);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error : "+error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", idx);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringReq);


    }

    @Override
    public int getItemCount() {

        return (listData !=null)?listData.size() : 0  ;
    }

    public class TemanViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView namaTxt,telponTxt;
        public  TemanViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.kartuku);
            namaTxt =(TextView) view.findViewById(R.id.textNama);
            telponTxt = (TextView) view.findViewById(R.id.textTelpon);
        }

    }
}
