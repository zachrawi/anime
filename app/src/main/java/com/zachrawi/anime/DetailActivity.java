package com.zachrawi.anime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "###";

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvSynopsis;

    private ProgressBar mProgressBar;
    private LinearLayout mMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mProgressBar = findViewById(R.id.progressBar);
        mMainContent = findViewById(R.id.llMainContent);

        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvSynopsis = findViewById(R.id.tvSynopsis);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        String url = "https://api.jikan.moe/v3/anime/" + id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(DetailActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        final String title = jsonObject.getString("title");
                        final String synopsis = jsonObject.getString("synopsis");
                        final String image = jsonObject.getString("image_url");

                        DetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(getApplicationContext())
                                        .load(image)
                                        .into(ivPoster);
                                tvTitle.setText(title);
                                tvSynopsis.setText(synopsis);

                                mProgressBar.setVisibility(View.INVISIBLE);
                                mMainContent.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
