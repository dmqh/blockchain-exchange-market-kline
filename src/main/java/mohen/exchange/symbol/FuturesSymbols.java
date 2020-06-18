package mohen.exchange.symbol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mohen.exchange.constant.FuturesConstant;

import java.util.Arrays;

/**
 * 交割合约币对
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public class FuturesSymbols extends AbstractSymbols {

    @Override
    public String uri() {
        return "https://api.hbdm.com/api/v1/contract_contract_info";
    }

    @Override
    protected void parse(String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        JSONArray data = (JSONArray) jsonObject.get(DATA);

        for (int i = 0; i < data.size(); i++) {
            JSONObject datum = (JSONObject) data.get(i);
            Arrays.stream(Suffix.values()).forEach(o -> SymbolParse.put(FuturesConstant.FUTURES, joint(o, datum.getString(SYMBOL))));
        }
    }

    private String joint(Suffix suffix, String symbol) {
        return (symbol.concat(suffix.toString())).toUpperCase();
    }

    enum Suffix {

        /**
         * 表示当周合约
         */
        _CW,

        /**
         * 表示次周合约
         */
        _NW,

        /**
         * 表示当季合约
         */
        _CQ,

        /**
         * 表示次季度合约
         */
        _NQ
    }
}
