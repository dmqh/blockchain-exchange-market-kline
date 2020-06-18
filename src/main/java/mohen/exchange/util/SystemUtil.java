package mohen.exchange.util;

/**
 * description
 * date 2020年5月19日
 *
 * @author yuanqiang.liu
 */
public class SystemUtil {

    /**
     * 系统属性 - 操作系统名称
     */
    private static final String PROPERTY_OS_NAME = "os.name";

    private static final String WINDOWS = "windows";
    private static final String JAR = "jar";

    private static final String FAILURE = "failure";
    private static final String COMPLETE = "complete";

    /**
     * 获取jar路径
     *
     * @return
     */
    public static String getJarPath() {
        String path = SystemUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty(PROPERTY_OS_NAME).toLowerCase().contains(WINDOWS)) {
            path = path.substring(1);
        }
        if (path.contains(JAR)) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }

    /**
     * 文件《失败的记录》
     *
     * @return
     */
    public static String getFailureFilePath() {
        return getJarPath().concat("/").concat(FAILURE);
    }

    /**
     * 文件《已完成的记录》 路径
     *
     * @return
     */
    public static String getCompleteFilePath() {
        return getJarPath().concat("/").concat(COMPLETE);
    }
}
