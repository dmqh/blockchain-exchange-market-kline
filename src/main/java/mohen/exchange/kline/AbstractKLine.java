package mohen.exchange.kline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import mohen.exchange.BaseSyncRequest;
import mohen.exchange.dto.KLine;
import mohen.exchange.dto.KLineDetail;
import mohen.exchange.symbol.AbstractSymbols;
import mohen.exchange.util.ExcelSpotUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * 获取KEY线数据
 *
 * @author yuanqiang.liu
 * @date 2020年5月18日
 */
@Slf4j
public abstract class AbstractKLine extends BaseSyncRequest {

    /**
     * 币对存储信息
     */
    private AbstractSymbols symbolParse;

    public AbstractKLine(AbstractSymbols symbolParse) {
        this.symbolParse = symbolParse;
    }

    /**
     * K线图返回值的KEY
     */
    protected static final String DATA = "data";
    protected static final String CH = "ch";

    private static final String DOT = ".";

    /**
     * K线图的数据
     */
    protected static Map<String, List<KLine>> dataMap = new HashMap<>();

    /**
     * 同步get请求
     *
     * @param period    查询间隔
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @throws Exception
     */
    public void syncGet(String period, long startTime, long endTime) throws IOException, ParseException {
        Set<String> symbols = getSymbols();

        for (String symbol : symbols) {
            log.info("当前币种：{}", symbol);
            String url = getUrl(symbol, period, startTime, endTime);
            log.info("请求链接: {}", url);
            syncGet(url);
        }

        excel(startTime, endTime);
    }

    /**
     * 获取完整url
     *
     * @param symbol    币种对
     * @param period    时间间隔
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param limit     数据数量
     * @return
     */
    private String getUrl(String symbol, String period, long startTime, long endTime) {
        return uri().concat("?").concat(appendParam(symbol, period, startTime, endTime));
    }

    /**
     * 参数添加
     *
     * @param symbol    币种对
     * @param interval  时间间隔
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    protected abstract String appendParam(String symbol, String interval, long startTime, long endTime);

    /**
     * 获取币对信息
     *
     * @return
     * @throws IOException
     */
    protected Set<String> getSymbols() throws IOException {
        return symbolParse.getSymbols(sign());
    }

    /**
     * excel导出
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @throws IOException
     * @throws ParseException
     */
    protected void excel(long startTime, long endTime) throws IOException, ParseException {
        List<KLine> kLines = dataMap.get(sign());
        ExcelSpotUtil.export(sign(), kLines, startTime, endTime);
    }

    /**
     * K线图数据解析
     *
     * @param value
     */
    @Override
    protected void parse(String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        if (jsonObject.get(STATUS) != null && ERROR.toUpperCase().equals(jsonObject.get(STATUS).toString().toUpperCase())) {
            return;
        }
        JSONArray jsonArray = (JSONArray) jsonObject.get(DATA);
        String symbol = jsonObject.getString(CH);
        KLine line = new KLine();
        line.setProduct(symbol(symbol));

        List<KLineDetail> lineDetails = new ArrayList<>();
        for (Object obj : jsonArray) {
            JSONObject json = (JSONObject) obj;
            lineDetails.add(new KLineDetail(json.getLong("id"), json.getBigDecimal("close"), json.getBigDecimal("vol"), json.getBigDecimal("amount")));
        }
        line.setDetails(lineDetails);

        put(sign(), line);
    }

    /**
     * K线图数据储存至DATA MAP中
     *
     * @param key   标识
     * @param value K线图数据
     */
    protected void put(String key, KLine value) {
        List<KLine> list = dataMap.get(key);
        if (list == null) {
            synchronized (dataMap) {
                list = new ArrayList<>();
                list.add(value);
                dataMap.put(key, list);
            }
        } else {
            list.add(value);
        }
    }

    /**
     * 标识
     *
     * @return
     */
    protected abstract String sign();

    /**
     * 币对处理
     *
     * @param symbol
     * @return
     */
    protected String symbol(String symbol) {
        if (symbol.indexOf(DOT) > 0) {
            symbol = symbol.substring(symbol.indexOf(".") + 1);
        }
        if (symbol.indexOf(DOT) > 0) {
            symbol = symbol.substring(0, symbol.indexOf("."));
        }
        return symbol;
    }

    /**
     * 拼接参数
     *
     * @return
     */
    protected String appendParam() {
        return null;
    }
}
