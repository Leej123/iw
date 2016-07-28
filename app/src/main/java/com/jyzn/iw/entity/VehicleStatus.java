package com.jyzn.iw.entity;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class VehicleStatus {
    public FunCode funCode = FunCode.CURRENT_STATUS;
    public enum WorkStatus {
        IDLE,				//空闲
        BREAK_DOWN,			//故障
        CHARGING,			//充电
        TAKING_SHELVES,		//取货架
        TO_PICKING_TABLE,	//送检货台
        LINE_UP,			//排队
        BACK_SHELVES		//归位货架
    }

    /**
     * ip地址作为小车的唯一标识
     */
    public String ip = "";

    /**
     * 工作状态
     */
    public WorkStatus workStatus = WorkStatus.IDLE;
    /**
     * 剩余电量
     */
    public int powerLeft = 100;
    /**
     * 坐标
     */
    public Coordinate coordinate = new Coordinate(0, 0, 0);
}
