package mohen.exchange.symbol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mohen.exchange.constant.SwapConstant;

/**
 * 交割合约币对
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
public class SwapSymbols extends AbstractSymbols {

    private static final String CONTRACT_CODE = "contract_code";

    @Override
    public String uri() {
        return "https://api.hbdm.com/swap-api/v1/swap_contract_info";
    }

    @Override
    protected void parse(String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        JSONArray data = (JSONArray) jsonObject.get(DATA);

        for (int i = 0; i < data.size(); i++) {
            JSONObject datum = (JSONObject) data.get(i);
            SymbolParse.put(SwapConstant.SWAP, datum.getString(CONTRACT_CODE));
        }
    }
}
