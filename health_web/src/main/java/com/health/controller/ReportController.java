package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.service.MemberService;
import com.health.service.ReportService;
import com.health.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @author W.Sun
 * @date 2019/11/11 9:22
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    /**
     * 会员数量折线图
     * @return         会员数据
     */
    @RequestMapping(value = "/getMemberReport", method = RequestMethod.GET)
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);//获得当前日期之前12个月的日期
        ArrayList<String> months = new ArrayList<>();//存放年月
        for (int i = 1; i <= 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        Map map = new HashMap();
        map.put("months", months);

        //数据信息要从数据库查
        List<Integer> memberCount = memberService.findMemberCount(months);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 返回套餐图表数据
     * @return      套餐图表数据
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            Map<String, Object> map = setmealService.getSetmealReport();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 返回运营数据
     */
    @RequestMapping(value = "/getBusinessReportData", method = RequestMethod.GET)
    public Result getBusinessReportData(){
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取运营数据
            Map<String, Object> reportData = reportService.getBusinessReportData();
            //获取模板路径
            String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator + "report_template.xlsx";
            //获取Excel模板对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //通过第三方模板技术，jxl填充EXCEL模板
            XLSTransformer xlsTransformer = new XLSTransformer();
            xlsTransformer.transformWorkbook(xssfWorkbook, reportData);
            //通过输出流返回页面下载至本地磁盘
            ServletOutputStream os = response.getOutputStream();
            //设置文件名，文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            //写出文件
            xssfWorkbook.write(os);
            //释放资源
            os.flush();
            os.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return null;
    }

    //动态获取指定日期区间的会员数量
    @RequestMapping(value = "/getDynamicMemberCount", method = RequestMethod.POST)
    public Result getDynamicMemberCount(@RequestBody Map m) {

        try {
            //parmas: {startDate: "2019-01", endDate: "2019-11"}

            String startDate = (String) m.get("startDate");
            String endDate = (String) m.get("endDate");

            Calendar calendar = Calendar.getInstance();//获取日期对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            Date date01 = sdf.parse(startDate);//将字符串格式转换成日期格式
            calendar.setTime(date01);//设置开始日期
            int year01 = calendar.get(Calendar.YEAR);//获取开始时间的年份
            int month01 = calendar.get(Calendar.MONTH);//获取开始时间的月份

            Date date02 = sdf.parse(endDate);//将字符串格式转换成日期格式
            calendar.setTime(date02);//设置结束日期
            int year02 = calendar.get(Calendar.YEAR);//获取结束时间的年份
            int month02 = calendar.get(Calendar.MONTH);//获取结束时间的月份

            //判断
            int result;
            if (year01 == year02) {
                result = month01 - month02-1;//结果为负数
            } else {
                result = 12 * (year01 - year02) + month01 - month02-1;
                if (result > 0) {
                    result = result * (-1);//取反
                }
            }
            calendar.add(Calendar.MONTH, result);//获得当前日期往前推result个月的日期
            //横轴需要展示的年月
            ArrayList<String> months=new ArrayList<>();//存放年月
            for (int i=1;i<=Math.abs(result);i++){
                calendar.add(Calendar.MONTH,1);
                months.add(sdf.format(calendar.getTime()));
            }
            Map map=new HashMap();
            map.put("months",months);
            //数据信息要从数据库查
            List<Integer> memberCount = memberService.findMemberCount(months);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 会员性别饼形图
     */
    @RequestMapping(value = "/getmemberSex", method = RequestMethod.GET)
    public Result getmemberSex() {
        try {
            Map<String, Object> map = memberService.findMemberSex();
            return new Result(true, MessageConstant.GET_MEMBER_SEX_SUCCESS, map);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_MEMBER_SEX_FAIL);
        }
    }

    /**
     * 会员年龄饼形图
     */
    @RequestMapping(value = "/getmemberAge", method = RequestMethod.GET)
    public Result getmemberAge() {
        try {
            Map<String, Object> map = memberService.findMemberAge();
            return new Result(true, MessageConstant.GET_MEMBER_AGE_SUCCESS, map);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_MEMBER_AGE_FAIL);
        }
    }
}
