package com.ghazy.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddSong extends AppCompatActivity {
    private EditText etName, etArtist, etAlbum, etDate;
    private Spinner spCategory;
    private ImageView ivCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        etName = findViewById(R.id.etNameAddSong);
        etArtist = findViewById(R.id.etArtistAddSong);
        etAlbum = findViewById(R.id.etAlbumAddSong);
        etDate = findViewById(R.id.etDateAddSong);
        spCategory = findViewById(R.id.spCategoryAddSong);
        ivCover = findViewById(R.id.ivCoverAddSong);

        spCategory.setAdapter(new ArrayAdapter<songCategory>(this, android.R.layout.simple_list_item_1, songCategory.values()));
    }


}