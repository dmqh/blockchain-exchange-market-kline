package mohen.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * K线图详细信息
 *
 * @author yuanqiang.liu
 * @date 2020年6月17日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KLineDetail {


    /**
     * 数据时间
     */
    private Long timestamp;

    /**
     * 收
     */
    private BigDecimal close;

    /**
     * 交易量
     */
    private BigDecimal volume;

    /**
     * 成交额
     */
    private BigDecimal amount;
}
