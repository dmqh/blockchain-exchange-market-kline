package mohen.exchange.util;


import mohen.exchange.dto.KLine;
import mohen.exchange.dto.KLineDetail;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static mohen.exchange.util.TimeUtil.nextDay;
import static mohen.exchange.util.TimeUtil.parseString;

/**
 * CSV 文件工具类
 *
 * @author yuanqiang.liu
 * @date 2020年5月19日
 */
public class ExcelSpotUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelSpotUtil.class);

    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    /**
     * 导出数据
     *
     * @param kLines    数据源
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @throws IOException
     */
    public static void export(String sign, List<KLine> kLines, Long startTime, Long endTime) throws IOException, ParseException {
        //获得Workbook工作薄对象
        Workbook workbook = new XSSFWorkbook();

        int dayCount = TimeUtil.dayCount(startTime, endTime);
        List<String> daySet = new ArrayList<>();
        Long time = startTime;
        for (int i = 0; i < dayCount; i++) {
            daySet.add(parseString(time));
            time = TimeUtil.nextDay(time);
        }
        daySet.add(parseString(endTime));

        Sheet amountSheet = workbook.createSheet("成交额");
        putSheet("日期", daySet, amountSheet);
        Sheet volumeSheet = workbook.createSheet("成交量");
        putSheet("日期", daySet, volumeSheet);
        Sheet closeSheet = workbook.createSheet("收盘价");
        putSheet("日期", daySet, closeSheet);

        List<String> collect = kLines.stream().map(o -> o.getProduct()).collect(Collectors.toList());
        Map<String, KLine> lineMap = kLines.stream().collect(Collectors.toMap(KLine::getProduct, Function.identity()));
        for (String symbol : collect) {
            Map<String, String> amountMap = lineMap.get(symbol).getDetails().stream().collect(Collectors.toMap(o -> parseString(o.getTimestamp()), o -> o.getAmount().toPlainString(), (k1, k2) -> k2));
            putSheet(symbol, amountMap, amountSheet);

            Map<String, String> volumeMap = lineMap.get(symbol).getDetails().stream().collect(Collectors.toMap(o -> parseString(o.getTimestamp()), o -> o.getVolume().toPlainString(), (k1, k2) -> k2));
            putSheet(symbol, volumeMap, volumeSheet);

            Map<String, String> closeMap = lineMap.get(symbol).getDetails().stream().collect(Collectors.toMap(o -> parseString(o.getTimestamp()), o -> o.getClose().toPlainString(), (k1, k2) -> k2));
            putSheet(symbol, closeMap, closeSheet);
        }

        File file = new File(getFilePath(sign, daySet.get(0), daySet.get(daySet.size() - 1)));
        OutputStream output = new FileOutputStream(file);
        workbook.write(output);
        output.flush();
        output.close();
    }

    /**
     * 获取文件路径
     *
     * @param sign  数据类型（现货、交割、永续等）
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static String getFilePath(String sign, String start, String end) {
        return SystemUtil.getJarPath().concat(sign).concat("_").concat(start).concat("_").concat(end).concat("_").concat(String.valueOf(System.currentTimeMillis())).concat(".").concat(XLSX);
    }

    /**
     * 将币对数据信息写入sheet
     *
     * @param rowName 币对名称
     * @param map     数据源
     * @param sheet   sheet页
     */
    private static void putSheet(String rowName, Map<String, String> map, Sheet sheet) {
        Row row = sheet.getRow(0);
        int physicalNumberOfCells = 0;
        if (row == null) {
            row = sheet.createRow(0);
        } else {
            physicalNumberOfCells = row.getPhysicalNumberOfCells();
        }

        row.createCell(physicalNumberOfCells).setCellValue(rowName);

        int rowCount = sheet.getLastRowNum();

        for (int i = 1; i <= rowCount; i++) {
            Row sheetRow = sheet.getRow(i);
            if (sheetRow == null) {
                continue;
            }
            Cell firstRowCell = sheetRow.getCell(0);
            if (firstRowCell == null) {
                continue;
            }
            String cellValue = map.get(firstRowCell.getStringCellValue());
            if (cellValue == null) {
                continue;
            }
            Row nextRow = sheet.getRow(i);
            if (nextRow == null) {
                nextRow = sheet.createRow(i);
            }
            Cell cell = nextRow.getCell(physicalNumberOfCells);
            if (cell == null) {
                cell = nextRow.createCell(physicalNumberOfCells);
            }
            cell.setCellValue(cellValue);
        }
    }

    /**
     * 将数据写入sheet
     *
     * @param rowName 表头名
     * @param list    数据源
     * @param sheet   sheet页
     */
    private static void putSheet(String rowName, List<String> list, Sheet sheet) {
        Row row = sheet.getRow(0);
        int physicalNumberOfCells = 0;
        if (row == null) {
            row = sheet.createRow(0);
        } else {
            physicalNumberOfCells = row.getPhysicalNumberOfCells();
        }

        row.createCell(physicalNumberOfCells).setCellValue(rowName);

        int i = 1;
        for (String value : list) {
            Row nextRow = sheet.getRow(i);
            if (nextRow == null) {
                nextRow = sheet.createRow(i);
            }
            Cell cell = nextRow.getCell(physicalNumberOfCells);
            if (cell == null) {
                cell = nextRow.createCell(physicalNumberOfCells);
            }
            cell.setCellValue(value);

            i++;
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        List<KLine> lines = new ArrayList<>();

        KLine line = new KLine();
        line.setProduct("test");

        Long ts = 1529164800000L;
        List<KLineDetail> lineDetails = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            KLineDetail detail = new KLineDetail();
            detail.setAmount(new BigDecimal(i));
            detail.setVolume(new BigDecimal(i));
            detail.setClose(new BigDecimal(i));
            detail.setTimestamp(ts);
            ts = nextDay(ts);

            if (i > 50 && i < 60) {
                continue;
            }
            lineDetails.add(detail);
        }

        line.setDetails(lineDetails);
        lines.add(line);

        KLine line2 = new KLine();
        line2.setProduct("mohen");

        Long ts2 = 1529164800000L;
        List<KLineDetail> lineDetails2 = new ArrayList<>();
        for (int i = 100; i < 200; i++) {

            KLineDetail detail = new KLineDetail();
            detail.setAmount(new BigDecimal(i));
            detail.setVolume(new BigDecimal(i));
            detail.setClose(new BigDecimal(i));
            detail.setTimestamp(ts2);
            ts2 = nextDay(ts2);

            if (i > 140 && i < 150) {
                continue;
            }
            lineDetails2.add(detail);
        }

        line2.setDetails(lineDetails2);
        lines.add(line2);
        export("SPOT", lines, 1529164800000L, 1540310400000L);
    }
}
