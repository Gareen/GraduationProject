package cn.sams.service.score;

import cn.sams.common.util.BatchUpdateUtil;
import cn.sams.common.util.Chk;
import cn.sams.dao.score.GroupInitManagementDao;

import cn.sams.dao.score.ScoreManagementDao;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.dao.system.CourseDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.*;

import cn.sams.entity.commons.ReturnObj;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/4/15.
 */
@Service("scoreManagementService")
public class ScoreManagementService {

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @Resource
    private HomeWorkManagementService homeWorkManagementService;

    @Resource
    private GroupInitManagementDao groupInitManagementDao;

    @Resource
    private StudentManagementDao studentManagementDao;

    @Resource
    private TermManagementDao termManagementDao;

    @Resource
    private ClassManagementDao classManagementDao;

    @Resource
    private CourseDao courseDao;

    @Resource
    private ScoreManagementDao scoreManagementDao;

    public List<Map<String, String>> query(HttpServletRequest req) {

        String classId = req.getParameter("classId");
        String teaNo = req.getParameter("teaNo");

        // 获得这学期分组的id
        String groupId = groupInitManagementService.getEncodeGroupId(req);

        // 获得这学期作业的id
        String homeWorkId = homeWorkManagementService.getWorkId(req);

        // 获得这学期的期末成绩id
        String finalId = getEncodeFinalId(req);

        if (!Chk.spaceCheck(groupId) || !Chk.spaceCheck(homeWorkId) ||
                !Chk.spaceCheck(finalId) || !Chk.spaceCheck(teaNo) || !Chk.spaceCheck(classId)) {
            return new ArrayList<>();
        }

        // !important 每次进行页面显示的时候都需要查询下最新的情况, 和下面的del顺序不可以互换
        List<FinalGrade> fgs = scoreManagementDao.queryFinalsByFinalId(finalId);

        // !important 此处先对当前查询条件下的表中数据进行一次清空, 意思就是每次打开的时候都要进行一次重新的数据插入
        delDataByFinalId(finalId);

        // 先根据班级的id查询所有的学生, 此处数据进行一次保留
        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        if (!Chk.emptyCheck(students)) {
            return new ArrayList<>();
        }

        // 根据分组编号查询出所有的分组成绩情况
        Map<String, Double> group_scores = countGroupStuScore(groupId);

        // 根据作业编号查询到每个学生对应的平均作业成绩
        Map<String, Double> work_scores = countHomeworkStuScore(homeWorkId);

        // 批量插入参数列表
        List<Object[]> argsList = new ArrayList<>();

        String updateSQL = "INSERT INTO sams_finalgrade VALUES (?, ?, ?, ?, ?, ?, ?)";

        for (Student student : students) {
            String stuNo = student.getStu_no();
            Object[] args;
            // 当原来的数据库中本来就没有记录的时候,
            if (!Chk.emptyCheck(fgs)) {

                args = new Object[]{finalId, work_scores.get(stuNo), group_scores.get(stuNo), null, null, stuNo, ""};
                argsList.add(args);

            } else {
                // 当原本的记录就存在的时候, 需要封装一套原本就有的记录
                for (FinalGrade fg : fgs) {

                    if (student.getStu_no().equals(fg.getFinal_stu_id())) {

                        args = new Object[]{
                                finalId, work_scores.get(stuNo), group_scores.get(stuNo),
                                fg.getFinal_exam_score(), fg.getFinal_score(), stuNo,
                                Chk.spaceCheck(fg.getFinal_remark()) ? fg.getFinal_remark() : ""
                        };

                        argsList.add(args);
                    }
                }
            }
        }

        BatchUpdateUtil.executeBatchUpdate(updateSQL, argsList);

        return getResultMap(students, finalId);
    }

    /**
     * 计算实验分组中每个成员的分数
     *
     * @param groupId 分组号
     * @return 每位学生的对应的实验分数 Map<学生学号, 实验分数>
     */
    private Map<String, Double> countGroupStuScore(String groupId) {

        // 从分组表中查询到一共多少个分组, 不从分组成绩表中进行查询, 因为存在大量的重复
        List<Group> groups = groupInitManagementDao.queryGroupsByGroupId(groupId);

        if (!Chk.emptyCheck(groups)) {
            return new HashMap<>();
        }

        // 查询实验次数, 实验次数为0, 说明没有实验成绩
        Integer expIndex = scoreManagementDao.queryExpIndex(groupId);

        if (expIndex <= 0) {
            return new HashMap<>();
        }

        // 存放学生学号和实验成绩的map
        Map<String, Double> map = new HashMap<>();
        for (Group group : groups) {

            String groupNum = group.getGroup_num();

            Double score = scoreManagementDao.countGroupScoreByGroupIdAndGroupNum(groupId, groupNum) / expIndex;

            map.put(group.getStu_is_leader(), score);

            String[] ids = group.getStu_is_member().split(",");

            for (String id : ids) {
                map.put(id, score);
            }
        }

        return map;
    }

    /**
     * 计算每个学生的平时作业的平均成绩
     *
     * @param workId 作业编号
     * @return Map<学生学号, 作业分数>
     */
    private Map<String, Double> countHomeworkStuScore(String workId) {
        // 先查一下次数, 如果次数为0的话, 直接返回一个空的集合
        List<Map<String, Double>> hws = scoreManagementDao.countHomeworkScoreByWorkId(workId);
        if (!Chk.emptyCheck(hws)) {
            return new HashMap<>();
        }

        Map<String, Double> maps = new HashMap<>();

        for (Map map : hws) {
            maps.put((String) map.get("stuNo"), (Double) map.get("sum"));
        }

        return maps;
    }

    /**
     * 组装最后返回给页面的结果集
     *
     * @param
     * @param students
     * @return
     */
    private List<Map<String, String>> getResultMap(List<Student> students, String finalId) {
        // 每次进行页面显示的时候都需要查询下最新的
        List<FinalGrade> fgs = scoreManagementDao.queryFinalsByFinalId(finalId);

        if (!Chk.emptyCheck(fgs)) {
            return new ArrayList<>();
        }

        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> map;

        for (FinalGrade fg : fgs) {
            map = new HashMap<>();

            map.put("finalId", fg.getFinal_id());
            map.put("stuNo", fg.getFinal_stu_id());

            map.put("stuName", "");
            for (Student student : students) {
                if (fg.getFinal_stu_id().equalsIgnoreCase(student.getStu_no())) {
                    map.put("stuName", student.getStu_name());
                    break;
                }
            }

            map.put("hScore", fg.getFinal_work_score());
            map.put("eScore", fg.getFinal_exp_score());
            map.put("fScore", fg.getFinal_exam_score());
            map.put("remark", fg.getFinal_remark());
            map.put("score", fg.getFinal_score());

            resultList.add(map);
        }

        return resultList;
    }

    /**
     * 先对表中的数据进行一次清空
     *
     * @param finalId
     * @return
     */
    private void delDataByFinalId(String finalId) {
        scoreManagementDao.delDataByFinalId(finalId);
    }


    /**
     * 获得学期的编号
     *
     * @param req
     * @return
     */
    public String getEncodeFinalId(HttpServletRequest req) {
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");
        String classId = req.getParameter("classId");

        // 如果一项为空直接返回空
        if (!Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId) || !Chk.spaceCheck(classId)) {
            return "";
        }
        return termId + "F" + courseId + "F" + classId;
    }


    /**
     * 保存方法
     *
     * @param req
     * @return
     */
    public ReturnObj save(HttpServletRequest req) {
        String finalId = req.getParameter("finalId");
        String stuNo = req.getParameter("stuNo");
        String datafield = req.getParameter("datafield");
        String value = req.getParameter("value");

        if (!Chk.spaceCheck(finalId) || !Chk.spaceCheck(finalId) ||
                !Chk.spaceCheck(finalId) || !Chk.spaceCheck(finalId)) {

            return new ReturnObj("error", "保存失败: 存在空值!", null);
        }

        // 如果保存的是分数的话
        if ("fScore".equalsIgnoreCase(datafield)) {
            String pss_cent = req.getParameter("p");
            String exs_cent = req.getParameter("e");
            String fis_cent = req.getParameter("f");

            if (!Chk.spaceCheck(pss_cent) || !Chk.spaceCheck(exs_cent) ||
                    !Chk.spaceCheck(fis_cent)) {
                return new ReturnObj("error", "保存失败: 分数比重不能为空 !", null);
            }

            try {
                double p = Double.parseDouble(pss_cent);
                double e = Double.parseDouble(exs_cent);
                double f = Double.parseDouble(fis_cent);

                double fs = Double.parseDouble(Chk.spaceCheck(value) ? value : "0.0");

                if (!((p + e + f - 1) < 0.001)) {
                    return new ReturnObj("error", "保存失败: 比重比例之和必须为100% !", null);
                }

                FinalGrade fg = scoreManagementDao.queryFgByFinalIdAndStuId(finalId, stuNo);

                double ps = Double.parseDouble(fg.getFinal_work_score());
                double es = Double.parseDouble(fg.getFinal_exp_score());

                // 保留0位小数
                double score = Math.round(ps * p + es * e + fs * f);

                Integer count = scoreManagementDao.saveScore(fs, score, finalId, stuNo);

                if (count < 1) {
                    return new ReturnObj("error", "保存失败: 数据库保存出错 !", null);
                }

                return new ReturnObj("success", "", score);

            } catch (NullPointerException e) {
                e.printStackTrace();
                return new ReturnObj("error", "保存失败: 重要数据格式转换异常 !", null);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new ReturnObj("error", "保存失败: 重要数据格式转换异常 !", null);

            } catch (Exception e) {
                e.printStackTrace();
                return new ReturnObj("error", "保存失败: 重要数据格式转换异常 !", null);
            }

        }

        if ("remark".equalsIgnoreCase(datafield)) {

            Integer count = scoreManagementDao.saveRemark(value, finalId, stuNo);

            if (count < 1) {
                return new ReturnObj("error", "保存失败: 数据库保存出错 !", null);
            }

            return new ReturnObj("success", "", null);

        }

        return new ReturnObj("error", "保存失败:datafield异常 !", null);


    }


    /**
     * 将数据导出
     *
     * @param req
     * @return
     */
    public void export(HttpServletRequest req, HttpServletResponse response) throws IOException {

        String name = "南京晓庄学院学生成绩单";
        XSSFWorkbook wb = new XSSFWorkbook();

        List<Map<String, Object>> argsList = getExportDataList(req);

        if (!Chk.emptyCheck(argsList)) {
            return;
        }

        String termId = req.getParameter("termId");
        String termName = termManagementDao.queryTermByTermId(termId).getTerm_name();

        // 进行组装
        doExport(wb, name, name, termName, argsList);

        // 将文件上传到页面
        OutputStream ouputStream = null;
        try {
            response.setContentType("application/x-msdownloadoctet-stream");
            String wordName = URLEncoder.encode(name, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + wordName + ".xlsx");
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

    public Map<String, String> getInfoData(HttpServletRequest req) {
        Map<String, String> m = new HashMap<>();

        String classId = req.getParameter("classId");
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");
        String teaNo = req.getParameter("teaNo");

        Course course = courseDao.queryCourse(courseId, teaNo, termId);

        if (course == null) {
            return new HashMap<>();
        }

        // todo

        return new HashMap<>();

    }

    public List<Map<String, Object>> getExportDataList(HttpServletRequest req) {
        String finalId = getEncodeFinalId(req);

        if (!Chk.spaceCheck(finalId)) {
            return new ArrayList<>();
        }

        String classId = req.getParameter("classId");

        if (!Chk.spaceCheck(classId)) {
            return new ArrayList<>();
        }

        String class_name = classManagementDao.queryClassByClassId(classId).getClass_name();
        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        if (!Chk.emptyCheck(students)) {
            return new ArrayList<>();
        }

        // 参数列表
        List<Map<String, Object>> argsList = new ArrayList<>();

        List<FinalGrade> finalGrades = scoreManagementDao.queryFinalsByFinalId(finalId);

        Map<String, Object> map;

        for (int i = 0; i < finalGrades.size(); ) {
            map = new HashMap<>();

            FinalGrade f = finalGrades.get(i);

            map.put("ID", ++i);
            map.put("STUNO", f.getFinal_stu_id());
            for (Student s : students) {

                if (f.getFinal_stu_id().equalsIgnoreCase(s.getStu_no())) {
                    map.put("STUNAME", s.getStu_name());
                }

            }
            map.put("CLASS", class_name);
            map.put("XDXZ", "");
            map.put("PS", f.getFinal_work_score());
            map.put("QZ", "");
            map.put("SY", f.getFinal_exp_score());
            map.put("QM", f.getFinal_exam_score());
            map.put("REMARK", f.getFinal_remark());
            map.put("SCORE", f.getFinal_score());

            argsList.add(map);
        }

        return argsList;
    }

    public void doExport(XSSFWorkbook wb, String tit, String sheetName, String termName, List<Map<String, Object>> list) {

        String colEn = "ID,STUNO,STUNAME,CLASS,XDXZ,PS,QZ,SY,QM,REMARK,SCORE";
        String[] colsEn = colEn.split(",");

        // 其他目前给的是实验的分数
        String colCn = "序号,学号,姓名,班级,修读性质,平时,期中,其他,期末,特殊原因,总评";
        String[] colsCn = colCn.split(",");

        // 在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);

        //单元格合并
        sheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0, (short) (colsEn.length - 1)));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) 0, (short) (colsEn.length - 1)));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, (short) 0, (short) (colsEn.length - 1)));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, (short) 0, (short) (colsEn.length - 1)));

        XSSFFont title = wb.createFont();
        title.setFontName("黑体");
        title.setFontHeightInPoints((short) 16);
        title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗

        XSSFFont column = wb.createFont();
        column.setFontName("宋体");
        column.setFontHeightInPoints((short) 11);
        column.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗

        XSSFFont cellValue = wb.createFont();
        cellValue.setFontName("宋体");
        cellValue.setFontHeightInPoints((short) 10);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 竖直居中
        titleStyle.setWrapText(false);
        titleStyle.setFont(title);

        XSSFCellStyle termStyle = wb.createCellStyle();
        termStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        termStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 竖直居中
        termStyle.setWrapText(false);
        termStyle.setFont(cellValue);

        XSSFCellStyle infoStyle = wb.createCellStyle();
        infoStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居左
        infoStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 竖直居中
        infoStyle.setWrapText(false);
        infoStyle.setFont(cellValue);

        XSSFCellStyle columnStyle = wb.createCellStyle();
        columnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        columnStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 竖直居中
        columnStyle.setWrapText(false);
        columnStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        columnStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        columnStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        columnStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        columnStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        columnStyle.setFont(column);

        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 竖直居中
        cellStyle.setWrapText(false);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        cellStyle.setFont(cellValue);


        // 表头
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(tit);
        cell.setCellStyle(titleStyle);

        // 学期
        XSSFRow term = sheet.createRow(1);
        XSSFCell termCell = term.createCell(0);
        termCell.setCellValue(termName);
        termCell.setCellStyle(termStyle);

        // 信息
        // 第一行信息
        XSSFRow info1 = sheet.createRow(2);
        XSSFCell info1cell = info1.createCell(0);

        String info1Str = "课程号:         课序号:          课程名:        开课单位:        ";
        info1cell.setCellValue(info1Str);
        info1cell.setCellStyle(infoStyle);

        // 第二行信息
        XSSFRow info2 = sheet.createRow(3);
        XSSFCell info2cell = info2.createCell(0);

        String info2Str = "学分:      人数:       上课教师:       录入状态: 已录入";
        info2cell.setCellValue(info2Str);
        info2cell.setCellStyle(infoStyle);

        // 列名
        row = sheet.createRow(4);
        for (int i = 0; i < colsCn.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(colsCn[i]);
            cell.setCellStyle(columnStyle);
        }

        // 添加数据
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {

                row = sheet.createRow(i + 5);
                for (int j = 0; j < colsEn.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(list.get(i).get(colsEn[j]) == null ? "" : list.get(i).get(colsEn[j]).toString());
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        //自适应宽度
        for (int i = 0; i < colsEn.length; i++) {
            sheet.autoSizeColumn((short) i, true);
        }

        // 封装表尾 从参数列表后的
        int rowIndex = list.size() + 5;
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 2, (short) 0, (short) (colsEn.length - 1)));
        row = sheet.createRow(rowIndex);
        XSSFCell rowCell = row.createCell(0);
        rowCell.setCellValue("登分人:__________  登分日期:__________  " +
                "教研室主任(签字):__________  审核日期:___________");
    }

}
