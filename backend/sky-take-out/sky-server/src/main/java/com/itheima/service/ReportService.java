package com.itheima.service;

import com.itheima.pojo.vo.OrderReportVO;
import com.itheima.pojo.vo.SalesTop10ReportVO;
import com.itheima.pojo.vo.TurnoverReportVO;
import com.itheima.pojo.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
