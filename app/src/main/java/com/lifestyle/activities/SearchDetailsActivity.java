package com.lifestyle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lifestyle.R;
import com.lifestyle.adapters.HistoryAdapter;
import com.lifestyle.model.History;
import com.lifestyle.fragment.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchDetailsActivity extends AppCompatActivity {
    CardView clearData;
    CardView deleteAccount;
    CardView logout;
    LinearLayout profileImage;
    TextView profileUsername;
    RecyclerView history;
    TextView emptyRecyclerView;
    private HistoryAdapter adapter;
    private List<History> historyList;
    TextView secondCircle;
    TextView firstCircle;
    TextView thirdCircle;
    TextView secondCircleName;
    TextView firstCircleName;
    TextView thirdCircleName;
    int pushupsTotal;
    int situpsTotal;
    int squatsTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        clearData = findViewById(R.id.clearData);
        deleteAccount = findViewById(R.id.deleteAccount);
        logout = findViewById(R.id.LLlogout);
        profileImage = findViewById(R.id.llProfileImage);
        profileUsername = findViewById(R.id.tvProfileUsername);
        history = findViewById(R.id.rvHistory);
        emptyRecyclerView = findViewById(R.id.tvEmptyRecyclerView);
        firstCircleName = findViewById(R.id.tvName1);
        secondCircleName = findViewById(R.id.tvName2);
        thirdCircleName = findViewById(R.id.tvName3);
        firstCircle = findViewById(R.id.tv1);
        secondCircle = findViewById(R.id.tv2);
        thirdCircle = findViewById(R.id.tv3);

        clearData.setVisibility(View.INVISIBLE);
        deleteAccount.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) profileImage.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        profileImage.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String username = intent.getStringExtra("username");
        pushupsTotal = intent.getIntExtra("pushupsNo", 0);
        situpsTotal = intent.getIntExtra("situpsNo", 0);
        squatsTotal = intent.getIntExtra("squatsNo", 0);
        profileUsername.setText(username);

        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(this, historyList);
        history.setAdapter(adapter);
        history.setLayoutManager(new LinearLayoutManager(this));
        queryHistoryById(userId);
    }

    private void queryHistoryById(String userId) {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.include(History.KEY_USER);
        query.whereEqualTo("userId", userId);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> history, ParseException e) {
                if (e != null) {
                    return;
                }
                historyList.addAll(history);
                adapter.notifyDataSetChanged();
                if (historyList.size() == 0) {
                    emptyRecyclerView.setText("This person's history is empty");
                    emptyRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    emptyRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
        ProfileFragment.isDone(secondCircle, firstCircle, thirdCircle,
                secondCircleName, firstCircleName, thirdCircleName,
                pushupsTotal, situpsTotal, squatsTotal);
    }
}