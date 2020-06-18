package mohen.exchange.kline;

import mohen.exchange.constant.SwapConstant;
import mohen.exchange.symbol.AbstractSymbols;

/**
 * 永续K线图信息获取
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public class SwapKLine extends AbstractKLine {

    public SwapKLine(AbstractSymbols symbolParse) {
        super(symbolParse);
    }

    @Override
    protected String appendParam(String symbol, String interval, long startTime, long endTime) {
        StringBuffer sb = new StringBuffer();
        sb.append("contract_code").append("=").append(symbol).append("&");
        sb.append("period").append("=").append(interval).append("&");
        sb.append("from").append("=").append(startTime).append("&");
        sb.append("to").append("=").append(endTime);

        return sb.toString();
    }

    @Override
    public String uri() {
        return "https://api.hbdm.com/swap-ex/market/history/kline";
    }

    @Override
    protected String sign() {
        return SwapConstant.SWAP;
    }

    @Override
    protected String appendParam() {
        return null;
    }
}
