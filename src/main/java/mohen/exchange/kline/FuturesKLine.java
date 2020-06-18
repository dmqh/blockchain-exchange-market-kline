package mohen.exchange.kline;

import mohen.exchange.constant.FuturesConstant;
import mohen.exchange.symbol.AbstractSymbols;

/**
 * 交割K线图信息获取
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public class FuturesKLine extends AbstractKLine {

    public FuturesKLine(AbstractSymbols symbolParse) {
        super(symbolParse);
    }

    @Override
    protected String appendParam(String symbol, String interval, long startTime, long endTime) {
        StringBuffer sb = new StringBuffer();
        sb.append("symbol").append("=").append(symbol).append("&");
        sb.append("period").append("=").append(interval).append("&");
        sb.append("from").append("=").append(startTime).append("&");
        sb.append("to").append("=").append(endTime);

        return sb.toString();
    }

    @Override
    public String uri() {
        return "https://api.hbdm.com/market/history/kline";
    }

    @Override
    protected String sign() {
        return FuturesConstant.FUTURES;
    }

    @Override
    protected String appendParam() {
        return null;
    }
}
