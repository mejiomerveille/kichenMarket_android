package com.example.mervshops.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mervshops.Constant;
import com.example.mervshops.HomeActivity;
import com.example.mervshops.Models.Post;
import com.example.namespace.R;
import com.example.namespace.databinding.LayoutPostBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder>{
    private  LayoutPostBinding ui;
    private SharedPreferences preferences;
    private int position;
    private Context context;
    private ArrayList<Post> list;
    private ArrayList<Post> listAll;
    public PostsAdapter(Context context, ArrayList<Post> list,@NonNull LayoutPostBinding ui) {
        this.context = context;
        this.list = list;
        this.listAll=new ArrayList<>(list);
        preferences=context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
    }
    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post,parent,false);
        return new PostsHolder(view,ui);
    }
    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Post post =list.get(position);
        Picasso.get().load(Constant.URL+"storage/profiles/"+post.getUser().getPhoto()).into(holder.imgprofil);
        Picasso.get().load(Constant.URL+"storage/profiles/"+post.getPhoto()).into(holder.imgPost);
        holder.txtName.setText(post.getUser().getUsername());
        holder.txtCommande.setText("View all"+post.getCommandes());
        holder.txtLike.setText(post.getLikes()+"Likes");
        holder.txtDate.setText(post.getDate());
        holder.txtDesc.setText(post.getDesc());

        if(post.getUser().getId()==preferences.getInt("id",0)){
            holder.btnPostOption.setVisibility(View.VISIBLE);
        }else {
            holder.btnPostOption.setVisibility(View.GONE);
        }
        holder.btnPostOption.setOnClickListener(v->{
            PopupMenu popupMenu =new PopupMenu(context,holder.btnPostOption);
            popupMenu.inflate(R.menu.menu_search);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.item_edit:{
                            Intent intent =new Intent((HomeActivity)context, EditText.class);
                            intent.putExtra("postId",post.getId());
                            intent.putExtra("position",position);
                            intent.putExtra("text",post.getId());
                            context.startActivity(intent);
                            return true;
                        }
                        case  R.id.item_delete:{
                            deletePost(post.getId(),position);
                            return true;
                        }
                    }
                    return false;
                }
            });
            popupMenu.show();
        });
    }
    private void deletePost(int postId,int position){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Delete post?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest request = new StringRequest(Request.Method.POST, Constant.DELETE_POST, response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("success")) {
                            //delete the post in recycler view
                          list.remove(position);
                          notifyItemRemoved(position);
                          notifyDataSetChanged();
                          listAll.clear();
                          listAll.addAll(list);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

                }) {
                    //add token to header

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String token = preferences.getString("token", "");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("Authorization", "Bearer" + token);
                        return map;
                    }

                    //add params
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", postId + "");
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Post> filteredList =new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(listAll);
            }else {
                for (Post post : listAll) {
                    if (post.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())
                            || post.getUser().getUsername().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(post);
                    }
                }
            }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Post>) results.values);
            notifyDataSetChanged();
        }
    };
    public Filter getFilter() {
        return filter;
    }

    class  PostsHolder extends RecyclerView.ViewHolder{
        private LayoutPostBinding ui;
        private ImageView imgprofil,imgPost;
        private ImageButton btnPostOption,btnLike,btnCommande;
        private TextView txtName,txtDate,txtDesc,txtLike,txtCommande;

        public PostsHolder(@NonNull View itemView,@NonNull LayoutPostBinding ui) {
            super(ui.getRoot());
            this.ui=ui;
//            super(itemView);
            txtName=ui.tstPostName;
            txtDate=ui.textPostDate;
            txtDesc=ui.textPost;
            txtLike=ui.txtPostLike;
            txtCommande=ui.txtPostPaid;
            btnPostOption=ui.btnPostsOption;
            btnLike=ui.btnPostLike;
            btnCommande=ui.btnPostPaid;
            imgprofil=ui.imgPostProfile;
            imgPost=ui.LangPostPhoto;
            btnPostOption.setVisibility(View.GONE);
        }
    }
}