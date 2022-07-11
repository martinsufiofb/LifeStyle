package com.example.lifestyle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lifestyle.History;
import com.example.lifestyle.HistoryAdapter;
import com.example.lifestyle.LoginActivity;
import com.example.lifestyle.Pushups;
import com.example.lifestyle.R;
import com.example.lifestyle.SignupActivity;
import com.example.lifestyle.Situps;
import com.example.lifestyle.Squats;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static String TAG = "ProfileFragment";
    CardView logout;
    CardView clearData;
    CardView deleteAccount;
    String currentUserUsername;
    TextView profileUsername;
    TextView secondCircle;
    TextView firstCircle;
    TextView thirdCircle;
    TextView emptyRecyclerView;
    TextView secondCircleName;
    TextView firstCircleName;
    TextView thirdCircleName;
    CircleImageView profileImage;
    String defaultImage;
    int pushupsTotal;
    int situpsTotal;
    int squatsTotal;
    public static RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private List<History> historyList;
    private List<Pushups> pushupsList;
    private List<Situps> situpsList;
    private List<Squats> squatsList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUserUsername = ParseUser.getCurrentUser().getUsername();
        profileUsername = view.findViewById(R.id.tvProfileUsername);
        firstCircle = view.findViewById(R.id.tv1);
        secondCircle = view.findViewById(R.id.tv2);
        thirdCircle = view.findViewById(R.id.tv3);
        clearData = view.findViewById(R.id.clearData);
        deleteAccount = view.findViewById(R.id.deleteAccount);
        emptyRecyclerView = view.findViewById(R.id.tvEmptyRecyclerView);
        firstCircleName = view.findViewById(R.id.tvName1);
        secondCircleName = view.findViewById(R.id.tvName2);
        thirdCircleName = view.findViewById(R.id.tvName3);
        profileImage = view.findViewById(R.id.ivprofilepic);
        profileUsername.setText(currentUserUsername);
        logout =  view.findViewById(R.id.LLlogout);
        defaultImage = getProfileUrl(ParseUser.getCurrentUser().getObjectId());

        Glide.with(this)
                .load(defaultImage)
                .into(profileImage);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                deleteHistoryData(user);
                deletePushupsData(user);
                deleteSitupsData(user);
                deleteSquatsData(user);
            }
        });
        
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                deleteHistoryData(user);
                deletePushupsData(user);
                deleteSitupsData(user);
                deleteSquatsData(user);
                deleteUser(user);
            }
        });

        rvHistory = view.findViewById(R.id.rvHistory);
        historyList = new ArrayList<>();
        pushupsList = new ArrayList<>();
        situpsList = new ArrayList<>();
        squatsList = new ArrayList<>();
        adapter = new HistoryAdapter(getContext(), historyList);
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        queryHistory();
        queryPushups();
        querySitups();
        querySquats();
    }

    private void deleteUser(ParseUser user) {
        user.deleteInBackground();
        logout();
    }

    private void deleteSquatsData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Squats");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                squatsList.clear();
                squatsTotal = 0;
                isdone();
            }
        });
    }

    private void deleteSitupsData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Situps");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                situpsList.clear();
                situpsTotal = 0;
                isdone();
            }
        });
    }

    private void deletePushupsData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pushups");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                pushupsList.clear();
                pushupsTotal = 0;
                isdone();
            }
        });
    }

    private void deleteHistoryData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("History");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                historyList.clear();
                adapter.notifyDataSetChanged();
                emptyRecyclerView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void querySquats() {
        ParseQuery<Squats> query = ParseQuery.getQuery(Squats.class);
        query.include(Squats.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Squats>() {
            @Override
            public void done(List<Squats> squats, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting squats", e);
                    return;
                }
                squatsList.addAll(squats);
                for(int i = 0; i<squatsList.size(); i++){
                    squatsTotal+= Integer.parseInt(squatsList.get(i).getCount());
                }
                isdone();
            }
        });
    }

    private void querySitups() {
        ParseQuery<Situps> query = ParseQuery.getQuery(Situps.class);
        query.include(Situps.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Situps>() {
            @Override
            public void done(List<Situps> situps, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting situps", e);
                    return;
                }
                situpsList.addAll(situps);
                for(int i = 0; i<situpsList.size(); i++){
                    situpsTotal+= Integer.parseInt(situpsList.get(i).getCount());
                }
                isdone();
            }
        });
    }

    private void queryPushups() {
        ParseQuery<Pushups> query = ParseQuery.getQuery(Pushups.class);
        query.include(Pushups.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Pushups>() {
            @Override
            public void done(List<Pushups> pushups, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting pushups", e);
                    return;
                }
                pushupsList.addAll(pushups);
                for(int i = 0; i<pushupsList.size(); i++){
                    pushupsTotal+= Integer.parseInt(pushupsList.get(i).getCount());
                }
                isdone();
            }
        });
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm){
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());
        Collections.sort(list, Comparator.comparing(Entry::getValue));
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private void isdone() {
        HashMap<String, Integer> hm = new HashMap<>();
        List<TextView> l1 = new ArrayList<>();
        List<TextView> l2 = new ArrayList<>();
        l1.add(0, thirdCircle);
        l1.add(1, secondCircle);
        l1.add(2, firstCircle);
        l2.add(0, thirdCircleName);
        l2.add(1, secondCircleName);
        l2.add(2, firstCircleName);
        hm.put("Push ups", pushupsTotal);
        hm.put("Sit ups", situpsTotal);
        hm.put("Squats", squatsTotal);
        int i = 0;
        Map<String, Integer> hmSorted = sortByValue(hm);
        for (Map.Entry<String, Integer> en : hmSorted.entrySet()) {
            l1.get(i).setText(String.valueOf(en.getValue()));
            l2.get(i).setText(String.valueOf(en.getKey()));
            i++;
        }
    }

    private void queryHistory() {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.include(History.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> history, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue getting exercises", e);
                    return;
                }
                historyList.addAll(history);
                adapter.notifyDataSetChanged();
                if (historyList.size()==0){
                    emptyRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    emptyRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error signing out", e);
                    Toast.makeText(getContext(), "Error signing out", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToLoginActivity();
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
}