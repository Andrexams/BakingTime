package br.com.martins.bakingtime.utils;

/**
 * Created by Andre Martins dos Santos on 05/06/2018.
 */
public class MediaUtils {

    enum VideoFormats{
        mp4
    }

    enum ImageFormats{
        jpg,
        png,
        bmp
    }

    public static boolean isVideo(String url) {
        if (url != null && !url.equals("")) {
            try {
                if (VideoFormats.valueOf(getExtension(url)) != null) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isImage(String url){
        if(url != null && !url.equals("")){
            try{
                if(ImageFormats.valueOf(getExtension(url))!= null){
                    return true;
                }
            }catch (IllegalArgumentException e){
                return false;
            }
        }
        return false;
    }

    public static String getExtension(String url){
        return url.substring(url.lastIndexOf(".")+1);
    }
}
