package mohen.exchange.symbol;

import mohen.exchange.BaseSyncRequest;

import java.io.IOException;
import java.util.Set;

/**
 * 获取币对
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public abstract class AbstractSymbols extends BaseSyncRequest {

    protected static final String DATA = "data";
    protected static final String SYMBOL = "symbol";

    /**
     * 同步get请求
     *
     * @throws Exception
     */
    public void syncGet() throws IOException {
        syncGet(uri());
    }

    /**
     * 获取币对信息
     *
     * @param sign 标识
     * @return
     * @throws IOException
     */
    public Set<String> getSymbols(String sign) throws IOException {
        if (SymbolParse.get(sign) == null) {
            syncGet();
        }
        return SymbolParse.get(sign);
    }
}
