package com.desmond.squarecamera;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by TedPark on 2016. 8. 30..
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.GalleryViewHolder> {


    private ArrayList<PickerTile> pickerTiles;
    private Context context;


    public ImageGalleryAdapter(Context context) {
        pickerTiles = new ArrayList<>();
        this.context = context;
        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (imageCursor != null) {

                int count = 0;
                while (imageCursor.moveToNext() && count < 15) {
                    String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageFile = new File(imageLocation);
                    pickerTiles.add(new PickerTile(Uri.fromFile(imageFile)));
                    count++;

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }


    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photos_list_item, parent, false);
        GalleryViewHolder holder = new GalleryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, final int position) {

        PickerTile pickerTile = getItem(position);
        Uri uri = pickerTile.getImageUri();
        Picasso.with(context)
                .load(uri)
                .error(R.drawable.ic_insert_photo_white_24dp)
                .resize(96,96)
                .centerCrop()
                .into(holder.iv_thumbnail);
    }

    public PickerTile getItem(int position) {
        return pickerTiles.get(position);
    }

    @Override
    public int getItemCount() {
        return pickerTiles.size();
    }

    public static class PickerTile {

        protected final Uri imageUri;

        protected PickerTile(@NonNull Uri imageUri) {
            this.imageUri = imageUri;
        }

        @Nullable
        public Uri getImageUri() {
            return imageUri;
        }

    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_thumbnail;

        public GalleryViewHolder(View view) {
            super(view);
            iv_thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);

        }

    }


}
