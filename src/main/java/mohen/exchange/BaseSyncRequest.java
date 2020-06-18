package mohen.exchange;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * 基本同步请求
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public abstract class BaseSyncRequest {

    private static Logger logger = LoggerFactory.getLogger(BaseSyncRequest.class);

    /**
     * 状态
     */
    protected static final String STATUS = "status";

    /**
     * 异常标识
     */
    protected static final String ERROR = "error";


    /**
     * 同步get请求
     *
     * @throws Exception
     */
    protected void syncGet(String url) throws IOException {
        // 创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 创建一个请求
        Request request = new Request.Builder().url(url).build();
        // 返回实体
        Response response = okHttpClient.newCall(request).execute();
        // 判断是否成功
        if (response.isSuccessful()) {
            /*
                获取返回的数据，可通过response.body().string()获取，默认返回的是utf-8格式；
                string()适用于获取小数据信息，如果返回的数据超过1M，建议使用stream()获取返回的数据，
                因为string() 方法会将整个文档加载到内存中。
            */
            // 打印数据
            if (response != null && response.body() != null) {
                String value = response.body().string();
                response.close();
                parse(value);
            } else {
                logger.warn(MessageFormat.format("请求【{0}】返回值为空", url));
            }
        } else {
            logger.error(MessageFormat.format("请求【{0}】失败", url));
        }
    }


    /**
     * 返回币种对请求连接
     *
     * @return
     */
    protected abstract String uri();

    /**
     * 数据解析
     *
     * @param value
     */
    protected abstract void parse(String value);
}
