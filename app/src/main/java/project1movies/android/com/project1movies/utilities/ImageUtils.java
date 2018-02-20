package project1movies.android.com.project1movies.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void setPosterImage(Context context, String posterUrl, ImageView imageView){
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(posterUrl).into(imageView);
    }
}
