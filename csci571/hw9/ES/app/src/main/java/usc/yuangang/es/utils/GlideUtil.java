package usc.yuangang.es.utils;
import android.content.Context;

import com.bumptech.glide.request.RequestOptions;

public class GlideUtil {
    private static RequestOptions roundRequestOptions = null;

    /**
     * 获取圆角属性
     *
     * @param radius
     * @return
     */
    public static RequestOptions getRoundRe(Context context, int radius) {
        if (null == roundRequestOptions) {
            roundRequestOptions = new RequestOptions()
                    .transform(new GlideRoundTransform(context, radius));
        }
        return roundRequestOptions;
    }
 } 