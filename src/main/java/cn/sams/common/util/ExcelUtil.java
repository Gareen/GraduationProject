package cn.sams.common.util;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;


/**
 * Created by Fanpeng on 2017/5/1.
 */
public class ExcelUtil {

    /**
     * 将Excel进行输出
     *
     * @param wb
     * @param response
     * @param fileName
     */
    public static void exportExcel(XSSFWorkbook wb, HttpServletResponse response, String fileName) {
        OutputStream ouputStream = null;
        try {
            response.setContentType("application/x-msdownloadoctet-stream");
            String name = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + name + ".xlsx");
            ouputStream = response.getOutputStream();
            wb.write(ouputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (ouputStream != null) {
                    ouputStream.flush();
                    ouputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
