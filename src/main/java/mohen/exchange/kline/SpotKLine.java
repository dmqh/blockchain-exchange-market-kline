package mohen.exchange.kline;

import mohen.exchange.constant.SpotConstant;
import mohen.exchange.symbol.AbstractSymbols;

/**
 * description
 * date 2020年6月17日
 *
 * @author yuanqiang.liu
 */
public class SpotKLine extends AbstractKLine {

    public SpotKLine(AbstractSymbols symbolParse) {
        super(symbolParse);
    }

    @Override
    protected String appendParam(String symbol, String interval, long startTime, long endTime) {
        StringBuffer sb = new StringBuffer();
        sb.append("symbol").append("=").append(symbol).append("&");
        sb.append("period").append("=").append(interval).append("&");
        sb.append("from").append("=").append(startTime).append("&");
        sb.append("to").append("=").append(endTime).append("&");
        sb.append("size").append("=").append("2000");

        return sb.toString();
    }

    @Override
    public String uri() {
        return "https://api.huobi.pro/market/history/kline";
    }

    @Override
    protected String sign() {
        return SpotConstant.SPOT;
    }
}
