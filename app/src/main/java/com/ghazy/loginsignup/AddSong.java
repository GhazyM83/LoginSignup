package com.ghazy.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddSong extends AppCompatActivity {
    private static final String TAG = "AddSong";
    private EditText etName, etArtist, etAlbum, etDate;
    private Spinner spCategory;
    private ImageView ivCover;
    private FirebaseServices fbs;

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
        fbs = FirebaseServices.getInstance();
        spCategory.setAdapter(new ArrayAdapter<songCategory>(this, android.R.layout.simple_list_item_1, songCategory.values()));
    }

    public void add(View view) {
        String name, artist, album, date, category, photo;
        name = etName.getText().toString();
        artist = etArtist.getText().toString();
        album = etAlbum.getText().toString();
        date = etDate.getText().toString();
        category = spCategory.getSelectedItem().toString();
        if (ivCover.getDrawable() == null)
            photo = "no_image";
        else photo = ivCover.getDrawable().toString();

        if (name.trim().isEmpty() || artist.trim().isEmpty() || album.trim().isEmpty() ||
                date.trim().isEmpty() || category.trim().isEmpty() || photo.trim().isEmpty())
        {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Song song = new Song(name, artist, album, songCategory.valueOf(category), date);
        fbs.getFire().collection("songs")
                .add(song)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        ivCover.setDrawingCacheEnabled(true);
        ivCover.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) ivCover.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = fbs.getStorage().getReference();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    public void selectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),40);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 40) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        ivCover.setBackground(null);
                        ivCover.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}