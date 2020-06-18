package mohen.exchange.symbol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mohen.exchange.constant.SpotConstant;

/**
 * description
 * date 2020年6月17日
 *
 * @author yuanqiang.liu
 */
public class SpotSymbols extends AbstractSymbols {

    @Override
    public String uri() {
        return "https://api.huobi.pro/v1/common/symbols";
    }

    @Override
    protected void parse(String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        JSONArray data = (JSONArray) jsonObject.get(DATA);

        for (int i = 0; i < data.size(); i++) {
            JSONObject datum = (JSONObject) data.get(i);
            SymbolParse.put(SpotConstant.SPOT, datum.getString(SYMBOL));
        }
    }
}
