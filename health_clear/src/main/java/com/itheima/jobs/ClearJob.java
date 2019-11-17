package com.itheima.jobs;

import com.health.utils.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;

public class ClearJob {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtil.getDataSource());
    //定时清理数据库信息
    public void clearData(){
        System.out.println("开始清理");
        //每次执行删除保留天数超过距离今天半个月的数据
       String sql = "DELETE FROM t_ordersetting WHERE orderDate <= (SELECT DATE_ADD(SYSDATE(),INTERVAL -15 DAY))";
       jdbcTemplate.update(sql);
        System.out.println("清理结束");
    }
}
