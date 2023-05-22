package com.example.mervshops.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mervshops.Adapter.PostsAdapter;
import com.example.mervshops.Constant;
import com.example.mervshops.HomeActivity;
import com.example.mervshops.Models.Post;
import com.example.mervshops.Models.User;
import com.example.namespace.R;
import com.example.namespace.databinding.LayoutHomeBinding;
import com.example.namespace.databinding.LayoutPostBinding;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class HomeFragment extends Fragment {
    private View view;
    public static RecyclerView recyclerView;
    public static ArrayList<Post> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    private LayoutPostBinding ui;
    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view=inflater.inflate(R.layout.layout_home,container,false);
        init();
        return view;
    }
    private void init(){
        sharedPreferences=getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView=view.findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout=view.findViewById(R.id.swipe_home);
        toolbar=view.findViewById(R.id.tool_bar);
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        getPosts();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });
    }

    private void getPosts() {
        arrayList=new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest request =new StringRequest(Request.Method.GET,Constant.POSTS,response -> {
            try {
                JSONObject object=new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray array =new JSONArray(object.getString("posts"));
                    for(int i=0;i<array.length();i++){
                        JSONObject postObject = array.getJSONObject(i);
                        JSONObject userObject=postObject.getJSONObject("user");
                        User user=new User();
                        user.setId(userObject.getInt("id"));
                        user.setUsername(userObject.getString("name")+""+userObject.getString("lastname"));
                        user.setPhoto(userObject.getString("photo"));
                        Post post =new Post();
                        post.setId(postObject.getInt("id"));
                        post.setUser(user);
                        post.setLikes(postObject.getInt(""));
                        post.setCommandes(postObject.getInt("commender"));
                        post.setDate(postObject.getString("create at"));
                        post.setDesc(postObject.getString("description"));
                        post.setPhoto(postObject.getString("photo"));
                        post.setSelflike(postObject.getBoolean("self like"));

                        arrayList.add(post);
                    }
                    postsAdapter=new PostsAdapter(getContext(),arrayList,ui);
                    recyclerView.setAdapter(postsAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            refreshLayout.setRefreshing(false);
        },error -> {
            error.printStackTrace();
            refreshLayout.setRefreshing(false);
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                String token= sharedPreferences.getString("token","");
                HashMap<String,String> map=new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return  map;

            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }
}