package com.ghazy.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllSongs extends AppCompatActivity {
    private FirebaseServices fbs;
    private ArrayList<Song> songs;
    private Adapter adapter;
    private RecyclerView rvSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        fbs = FirebaseServices.getInstance();
        songs = new ArrayList<Song>();
        adapter = new Adapter(this, songs);
        rvSongs = findViewById(R.id.rvSongsAllSongs);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));
        rvSongs.setAdapter(adapter);
        readData();
    }

    private void readData() {
        fbs.getFire().collection("songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                songs.add(document.toObject(Song.class));
                            }
                        } else {
                            Log.e("ALlSongs: readData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}