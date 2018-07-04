package rk.com.demo_smartagent;

import android.app.DownloadManager;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import static android.content.ContentValues.TAG;


public class SmartAgentAdapter extends RecyclerView.Adapter<SmartAgentAdapter.MyViewHolder> {
    private List<SmartAgentPojo> smartAgentPojoList;

    private Context mcontext;

    String videourl;
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

        boolean ret = false;

        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        InputStream is = null;

        String url = smartAgentPojo.getCdn_path();


        if (url.length() == 0) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);

        } else if (url.toLowerCase().contains(".jpg")) {


            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            new DownloadImageTask(holder.imageView).execute(url);

        } else if (url.toLowerCase().contains(".mp4")) {


            videourl=url;



            holder.videoView.setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        public void run() {
                            downloadFile();
                        }
                    }).start();

            Uri myUri = Uri.parse(url);
            holder.videoView.setMediaController(new MediaController(mcontext));
            holder.videoView.setVideoURI(myUri);
            holder.videoView.requestFocus();
            holder.videoView.setMediaController(null);
            holder.videoView.start();


            holder.imageView.setVisibility(View.GONE);
        }


        holder.idd.setText("Id : " + smartAgentPojo.getId());

        holder.name.setText("Name : " + smartAgentPojo.getName());

        holder.type.setText("Type : " + smartAgentPojo.getType());

        holder.cdn_path.setText("Path : " + smartAgentPojo.getCdn_path());


        holder.size_byte.setText("Size : " + smartAgentPojo.getSize_byte());


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

                saveImageToExternalStorage(mIcon11);
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
    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images_1");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(mcontext, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                        System.out.println("path_is"+path);
                     

                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });


    }
    void downloadFile(){


        int downloadedSize=0;
        try {

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("example.com", session);
                }
            };

            URL url = new URL(videourl);

            System.out.println("videourl"+videourl);


            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);


            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Videossss");

            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdir();
                System.out.println("Download"+"Folder created123:"+"true");

            }else{
                System.out.println("Download"+"Folder created1234:"+"false");

            }


            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File file = new File(mediaStorageDir,"downloaded_file.mp4");

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
           int totalSize = urlConnection.getContentLength();


            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
            }
            //close the output stream when complete //
            fileOutput.close();

        } catch (final MalformedURLException e) {
         //   showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
           // showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
           // showError("Error : Please check your internet connection " + e);
        }
    }

}
