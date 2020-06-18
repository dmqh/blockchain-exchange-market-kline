package mohen.exchange.dto;

import lombok.Data;

import java.util.List;

/**
 * K线图数据
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
@Data
public class KLine {

    /**
     * 产品 (BTC-USDT)
     */
    private String product;

    /**
     * 详情数据
     */
    private List<KLineDetail> details;
}
