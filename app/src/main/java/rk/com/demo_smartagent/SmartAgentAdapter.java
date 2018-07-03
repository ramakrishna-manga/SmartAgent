package rk.com.demo_smartagent;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.content.ContentValues.TAG;


public class SmartAgentAdapter extends RecyclerView.Adapter<SmartAgentAdapter.MyViewHolder> {
    private List<SmartAgentPojo> smartAgentPojoList;

    private Context mcontext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView idd, name, type, cdn_path, size_byte;

        VideoView videoView;


        public MyViewHolder(View view) {


            super(view);

            mcontext = view.getContext();

            idd = (TextView) itemView.findViewById(R.id.iddd);
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            cdn_path = (TextView) itemView.findViewById(R.id.cdn_path);

            size_byte = (TextView) itemView.findViewById(R.id.size_byte);

            imageView = (ImageView) itemView.findViewById(R.id.imageview);

            videoView = (VideoView) itemView.findViewById(R.id.videoview);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.smartagent_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public SmartAgentAdapter(List<SmartAgentPojo> smartAgentPojoList) {

        this.smartAgentPojoList = smartAgentPojoList;


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SmartAgentPojo smartAgentPojo = smartAgentPojoList.get(position);


        String url = smartAgentPojo.getCdn_path();


        if (url.length() == 0) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);

        } else if (url.toLowerCase().contains(".jpg")) {

            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            new DownloadImageTask(holder.imageView).execute(url);

        } else if (url.toLowerCase().contains(".mp4")) {

            holder.videoView.setVisibility(View.VISIBLE);


            Uri myUri = Uri.parse(url);

            holder.videoView.setMediaController(new MediaController(mcontext));
            holder.videoView.setVideoURI(myUri);
            holder.videoView.requestFocus();
            holder.videoView.start();


            holder.imageView.setVisibility(View.GONE);
        }


        holder.idd.setText("Id : "+smartAgentPojo.getId());

        holder.name.setText("Name : "+smartAgentPojo.getName());

        holder.type.setText("Type : "+smartAgentPojo.getType());

        holder.cdn_path.setText("Path : "+smartAgentPojo.getCdn_path());



        holder.size_byte.setText("Size : "+smartAgentPojo.getSize_byte());


    }

    @Override
    public int getItemCount() {
        return smartAgentPojoList.size();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}