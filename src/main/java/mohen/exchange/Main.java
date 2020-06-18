package mohen.exchange;

import mohen.exchange.kline.AbstractKLine;
import mohen.exchange.kline.FuturesKLine;
import mohen.exchange.kline.SpotKLine;
import mohen.exchange.kline.SwapKLine;
import mohen.exchange.symbol.AbstractSymbols;
import mohen.exchange.symbol.FuturesSymbols;
import mohen.exchange.symbol.SpotSymbols;
import mohen.exchange.symbol.SwapSymbols;
import mohen.exchange.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;

/**
 * 项目入口
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public class Main {

    /**
     * VM options 与 args 二选一
     * <p>
     * VM options：
     * -Dperiod=1day -Dfrom=2019-06-18 -Dto=2020-06-18
     * <p>
     * -Dperiod K线周期
     * -Dfrom 时间范围 -> 开始时间（yyyy-MM-dd）
     * -Dto 时间范围 -> 结束时间（yyyy-MM-dd）
     *
     * @param args 指定参数，参数顺序：period from to
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        String period;
        String from;
        String to;
        if (args != null && args.length == 3) {
            period = args[0];
            from = args[1];
            to = args[2];
        } else {
            period = System.getProperty("period");
            from = System.getProperty("from");
            to = System.getProperty("to");
        }

        if (StringUtils.isBlank(period) || StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            System.exit(1);
        }

        long start = TimeUtil.parse(from, TimeUtil.SECOND);
        long end = TimeUtil.parse(to, TimeUtil.SECOND);

        /* 获取现货相关K线图数据 */
        AbstractSymbols spotSymbols = new SpotSymbols();
        AbstractKLine syncRequest = new SpotKLine(spotSymbols);
        syncRequest.syncGet(period, start, end);

        /* 获取交割合约相关K线图数据 */
        AbstractSymbols futuresSymbols = new FuturesSymbols();
        AbstractKLine futuresSyncRequest = new FuturesKLine(futuresSymbols);
        futuresSyncRequest.syncGet(period, start, end);

        /* 获取永续合约相关K线图数据 */
        AbstractSymbols swapSymbols = new SwapSymbols();
        AbstractKLine swapSyncRequest = new SwapKLine(swapSymbols);
        swapSyncRequest.syncGet(period, start, end);
    }
}
