package com.itheima.service;

import com.itheima.pojo.vo.BusinessDataVO;
import com.itheima.pojo.vo.DishOverViewVO;
import com.itheima.pojo.vo.OrderOverViewVO;
import com.itheima.pojo.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    DishOverViewVO getDishOverView();

    OrderOverViewVO getOrderOverView();

    SetmealOverViewVO getSetmealOverView();
}
