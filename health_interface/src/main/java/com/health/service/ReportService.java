package com.health.service;

import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/12 15:49
 */
public interface ReportService {
    /**
     * 获取运营数据
     * @return        运营数据
     */
    Map<String,Object> getBusinessReportData() throws Exception;
}
