package cn.sams.service.system;

import cn.sams.common.constants.Constant;
import cn.sams.common.util.BatchUpdateUtil;
import cn.sams.common.util.ExcelUtil;
import cn.sams.common.util.Chk;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.entity.Classes;
import cn.sams.entity.Student;
import cn.sams.entity.commons.ReturnObj;

import com.sun.xml.internal.xsom.impl.Const;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Service("studentManagementService")
public class StudentManagementService {

    @Resource
    private StudentManagementDao studentManagementDao;

    @Resource
    private ClassManagementDao classManagementDao;

    public List<Student> queryStudents(HttpServletRequest req) {

        List<Student> students = studentManagementDao.queryStudents();
        List<Student> s = new ArrayList<>();

        if (Chk.emptyCheck(students)) {

            for (Student student : students) {
                Student stu = new Student();

                stu.setStu_no(student.getStu_no());
                stu.setStu_name(student.getStu_name());
                stu.setStu_gender("0".equals(student.getStu_gender()) ? "男" : "女");

                String classId = student.getStu_class_id();
                Classes c = classManagementDao.queryClassByClassId(classId);

                stu.setStu_class_id((c != null && Chk.spaceCheck(c.getClass_name())) ? c.getClass_name() : "");

                s.add(stu);
            }
            return s;
        }

        return new ArrayList<>();
    }

    /**
     * 将学生信息模版导出
     *
     * @return
     */
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = "学生信息";
        XSSFWorkbook wb = new XSSFWorkbook();

        doExport(wb, fileName);

        ExcelUtil.exportExcel(wb, response, fileName);
    }

    private void doExport(XSSFWorkbook wb, String fileName) {

        // 在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(fileName);

        String[] colsCn = "学号,姓名,性别".split(Constant.SEPARATOR);

        XSSFFont column = wb.createFont();
        column.setFontName("黑体");
        column.setFontHeightInPoints((short) 11);
        column.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗

        XSSFCellStyle columnStyle = wb.createCellStyle();
        columnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        columnStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 竖直居中
        columnStyle.setWrapText(false);
        columnStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        columnStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        columnStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        columnStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        columnStyle.setFont(column);

        // 表头
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("班级编码");
        cell.setCellStyle(columnStyle);

        XSSFCell cell1 = row.createCell(2);
        cell1.setCellValue("班级名称");
        cell1.setCellStyle(columnStyle);

        // 列名
        row = sheet.createRow(1);
        for (int i = 0; i < colsCn.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(colsCn[i]);
            cell.setCellStyle(columnStyle);
        }

        //自适应宽度
        for (int i = 0; i < colsCn.length; i++) {
            sheet.autoSizeColumn((short) i, true);
        }

    }

    /**
     * 将学生信息导入到数据库中
     *
     * @param req
     * @return
     */
    public ReturnObj importStudents(HttpServletRequest req, HttpServletResponse res) {
        try {

            return saveTempFile(req, res);

        } catch (Exception e) {
            e.printStackTrace();

            return new ReturnObj(Constant.ERROR, e.getMessage(), null);
        }

    }

    /**
     * 将上传的excel进行解析入库
     *
     * @param req
     * @param res
     * @return
     * @throws FileUploadException
     */
    private ReturnObj saveTempFile(HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException {

        String serverPath = req.getServletContext().getRealPath("/").replace("\\", "/");

        String dirPath = serverPath + Constant.FILE_TEMP_PATH;

        if (!new File(dirPath).exists()) {
            new File(dirPath).mkdir();
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        //设置向硬盘写数据时所用的缓冲区的大小，此处为4K
        factory.setSizeThreshold(4 * 1024);

        // 设置临时目录
        factory.setRepository(new File(dirPath));

        //创建一个文件上传处理器
        ServletFileUpload upload = new ServletFileUpload(factory);

        //设置允许上传的文件的最大尺寸，此处为40M
        upload.setSizeMax(40 * 1024 * 1024);

        List<FileItem> items = upload.parseRequest(req);

        if (!Chk.emptyCheck(items)) {
            return new ReturnObj(Constant.ERROR, "没有文件 ！", null);
        }

        for (FileItem file : items) {

            if (!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")) {
                return new ReturnObj(Constant.ERROR, "上传的文件格式错误 ！", null);
            }

            ReturnObj b = saveData(file);

            if (Constant.ERROR.equalsIgnoreCase(b.getStatus())) {
                return new ReturnObj(Constant.ERROR, b.getMsg(), b.getData());
            }
        }

        return new ReturnObj(Constant.SUCCESS, "", null);
    }

    /**
     * 将上传的数据解析入库
     */
    private ReturnObj saveData(FileItem file) throws IOException {

        String fileType = file.getContentType();

        InputStream fis = file.getInputStream();

        // 批量插入参数列表
        List<Object[]> argsList = new ArrayList<>();
        String updateSql = "INSERT INTO sams_student VALUES (?,?,?,?)";

        // 将小数转换成整数
        DecimalFormat df = new DecimalFormat("#");

        // Excel 2003
        if (fileType != null && "application/vnd.ms-excel".equalsIgnoreCase(fileType)) {

            POIFSFileSystem in = new POIFSFileSystem(fis);

            // 得到文档对象
            HSSFWorkbook workbook = new HSSFWorkbook(in);

            // 得到第一个工作表
            HSSFSheet aSheet = workbook.getSheetAt(0);

            if (aSheet == null) {
                return new ReturnObj(Constant.ERROR, "工作表内容为空", null);
            }

            int rowCounts = aSheet.getLastRowNum();

            // 当表中记录大于100行，判定出错
            if (rowCounts > 100) {
                return new ReturnObj(Constant.ERROR, "记录行数大于100", null);
            }

            for (int i = 0; i <= aSheet.getLastRowNum(); i++) {

                HSSFRow row = aSheet.getRow(i);

                // 班级编码
                String classId = "";
                // 班级名称
                String className;

                if (i == 0) {
                    // 班级编码
                    HSSFCell cell = row.getCell(1);
                    classId = Chk.spaceCheck(df.format(cell.getNumericCellValue())) ? df.format(cell.getNumericCellValue()).trim() : "";

                    // 班级名称
                    HSSFCell cell1 = row.getCell(3);
                    className = Chk.spaceCheck(cell1.getStringCellValue()) ? cell1.getStringCellValue().trim() : "";

                    // 根据classId查询有没有这个班级，如果有，则不做操作，如果没有，对class表做新增
                    Classes classes = classManagementDao.queryClassByClassId(classId);

                    // 当classId和className都不为空的情况下进行插入
                    if (classes == null && Chk.spaceCheck(classId) && Chk.spaceCheck(className)) {
                        int count = classManagementDao.insertClasses(classId, className);

                        if (count < 1) {
                            return new ReturnObj(Constant.ERROR, "解析新增班级错误!", null);
                        }
                    }
                }

                if (i == 1) {
                    // 跳过列名, 不对列名进行处理
                    continue;
                }

                String stuNo = df.format(row.getCell(0).getNumericCellValue());
                String stuName = row.getCell(1).getStringCellValue().trim();
                String genstr = row.getCell(2).getStringCellValue().trim();

                if ("男".equalsIgnoreCase(genstr)) {
                    genstr = "0";
                } else {
                    genstr = "1";

                }

                Object[] args = new Object[]{stuNo, stuName, genstr, classId};

                argsList.add(args);
            }

        }

        // Excel 2007
        if (fileType != null && "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(fileType)) {

            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // 得到第一个工作表
            XSSFSheet aSheet = workbook.getSheetAt(0);

            if (aSheet == null) {
                return new ReturnObj(Constant.ERROR, "工作表内容为空", null);
            }

            int rowCounts = aSheet.getLastRowNum();

            // 当表中记录大于100行，判定出错
            if (rowCounts > 100) {
                return new ReturnObj(Constant.ERROR, "记录行数大于100", null);
            }

            // 班级编码
            String classId = "";
            // 班级名称
            String className;

            for (int i = 0; i <= aSheet.getLastRowNum(); i++) {

                XSSFRow row = aSheet.getRow(i);

                if (i == 0) {
                    // 班级编码
                    XSSFCell cell = row.getCell(1);
                    classId = Chk.spaceCheck(df.format(cell.getNumericCellValue())) ? df.format(cell.getNumericCellValue()).trim() : "";

                    // 班级名称
                    XSSFCell cell1 = row.getCell(3);
                    className = Chk.spaceCheck(cell1.getStringCellValue()) ? cell1.getStringCellValue().trim() : "";

                    // 根据classId查询有没有这个班级，如果有，则不做操作，如果没有，对class表做新增
                    Classes classes = classManagementDao.queryClassByClassId(classId);

                    // 当classId和className都不为空的情况下进行插入
                    if (classes == null && Chk.spaceCheck(classId) && Chk.spaceCheck(className)) {
                        int count = classManagementDao.insertClasses(classId, className);

                        if (count < 1) {
                            return new ReturnObj(Constant.ERROR, "解析新增班级错误!", null);
                        }
                    }
                    continue;
                }

                if (i == 1) {
                    // 跳过列名, 不对列名进行处理
                    continue;
                }

                String stuNo = df.format(row.getCell(0).getNumericCellValue());
                String stuName = row.getCell(1).getStringCellValue().trim();
                String genstr = row.getCell(2).getStringCellValue().trim();

                if ("男".equalsIgnoreCase(genstr)) {
                    genstr = "0";
                } else {
                    genstr = "1";

                }

                Object[] args = new Object[]{stuNo, stuName, genstr, classId};

                argsList.add(args);
            }
        }

        BatchUpdateUtil.executeBatchUpdate(updateSql, argsList);

        return new ReturnObj(Constant.SUCCESS, "", null);
    }

}
