package zentech.myapplication.Entities.Singletons;

import android.app.Application;
import android.content.Context;

import zentech.myapplication.Entities.Singletons.ImageLoader;

/**
 * Created by izik on 1/3/2017.
 */

public class MyApplication extends Application
{
    private ImageLoader imageLoader;

    public ImageLoader getImageLoader()
    {
        if(imageLoader != null)
        {
            return imageLoader;
        }
        else {

            return null;
        }
    }

    public void setImageLoader(Context context)
    {
        this.imageLoader = new ImageLoader(context);
    }
}
