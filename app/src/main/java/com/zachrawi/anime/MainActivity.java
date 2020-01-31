package com.zachrawi.anime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "###";
    private int mPage = 1;

    private ArrayList<Anime> mAnimes;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AnimeAdapter mAnimeAdapter;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnimes = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerView);
        mProgressBar = findViewById(R.id.progressBar);

        mAnimeAdapter = new AnimeAdapter(this, R.layout.item_anime, mAnimes, new AnimeAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Anime anime = mAnimes.get(position);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", anime.getId());

                startActivity(intent);
            }
        });

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setAdapter(mAnimeAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadPage();
    }

    private void loadPage() {
        String url = "https://api.jikan.moe/v3/top/anime/" + mPage + "/tv";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(json);

                        JSONArray results = jsonObject.getJSONArray("top");
                        for (int i=0;i<results.length();i++) {
                            JSONObject result = results.getJSONObject(i);

                            int id = result.getInt("mal_id");
                            String title = result.getString("title");
                            String image = result.getString("image_url");

                            mAnimes.add(new Anime(id, title, image));
                        }

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                mRecyclerView.setVisibility(View.VISIBLE);

                                mAnimeAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
