package com.bdqn.controller;


import cn.itrip.common.DateUtil;
import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripHotelRoom.ItripHotelRoomMapper;
import cn.itrip.dao.itripHotelTempStore.ItripHotelTempStoreMapper;
import cn.itrip.dao.itripImage.ItripImageMapper;
import cn.itrip.pojo.*;
import com.bdqn.biz.HotelServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.rmi.CORBA.Util;
import java.util.*;

@Controller
@RequestMapping("/api")
public class HotelContoller {

    @Resource
    ItripHotelRoomMapper dao;

    @Resource
    ItripImageMapper dao1;

    @Resource
    ItripAreaDicMapper dao2;

    @Resource
    ItripHotelTempStoreMapper dao3;


    //获取商圈
    @RequestMapping(value = "/hotel/querytradearea/{cityId}",method=RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto GetAreaByCity(@PathVariable("cityId")Integer cityid)
    {
         return DtoUtil.returnDataSuccess(dao2.GetAreaBySearch(cityid));
    }

    //根据房间ID 查询房间图片
    @RequestMapping(value = "/hotelroom/getimg/{targetId}",method=RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto GetRoomByImage(@PathVariable("targetId")String targetId)
    {
        return  DtoUtil.returnSuccess("获取数据成功",dao1.Get_1(targetId));
    }



  @RequestMapping(value = "/hotelorder/getpreorderinfo",method=RequestMethod.POST,produces = "application/json")
  @ResponseBody
  public Dto GetList(@RequestBody ValidateRoomStoreVO vo)
  {
        //第一步，如果临时库存表没有数据，那么要去原始库存表导入数据
   //   {call Insert_Data2_325(#{hid},#{rid},#{sd},#{ed})}
      Map<String,Object> map=new HashMap<>();
      map.put("hid",vo.getHotelId());
      map.put("rid",vo.getRoomId());
      map.put("sd",vo.getCheckInDate());
      map.put("ed",vo.getCheckOutDate());
      dao3.In_data(map);
       //返回该时间段内的所有日期的房间数量
      Map<String,Object> map1=new HashMap<>();
      map1.put("rid",vo.getRoomId());
      map1.put("sd",vo.getCheckInDate());
      map1.put("ed",vo.getCheckOutDate());
      List<StoreVO> list=dao3.GetList(map1);

      RoomStoreVO vo1=new RoomStoreVO();
      vo1.setHotelName("酒店名字");

      vo1.setStore(list.get(0).getStore());
      return DtoUtil.returnSuccess("获取成功",vo1);






      //取所有房间数量的最小值及房间信息付给roomstorevo 返回给客户端

  }

    //根据酒店id 获取房间
    @RequestMapping(value = "/hotelroom/queryhotelroombyhotel",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto<List<ItripHotelRoomVO>> GetHomeRom(@RequestBody SearchHotelRoomVO vo)
    {
        //通过一个类  把开始时间和结束时间传入进去，返回一个list
     /*    vo.getStartDate();
         vo.getEndDate();
         vo.getHotelId();*/
        List<List<ItripHotelRoomVO>> hotelRoomVOList = null;
        List<Date> dlist=DateUtil.getBetweenDates( vo.getStartDate(),vo.getEndDate());

        Map<String,Object> map=new HashMap<>();
        map.put("hotelId",vo.getHotelId());
        map.put("dlist",dlist);
        List<ItripHotelRoomVO> originalRoomList=dao.GetRoomByDate(map);
      /*  for (ItripHotelRoomVO roomVO : originalRoomList) {
            List<ItripHotelRoomVO> tempList = new ArrayList<ItripHotelRoomVO>();
            tempList.add(roomVO);
            hotelRoomVOList.add(tempList);
        }*/

        hotelRoomVOList = new ArrayList();
        for (ItripHotelRoomVO roomVO : originalRoomList) {
            List<ItripHotelRoomVO> tempList = new ArrayList<ItripHotelRoomVO>();
            tempList.add(roomVO);
            hotelRoomVOList.add(tempList);
        }
        return DtoUtil.returnSuccess("获取成功", hotelRoomVOList);
    }
    @RequestMapping(value = "hotel/queryhotelfeature",method=RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryhotelfeature()
    {
       List<ItripLabelDic> list=biz.GetDataFrea();
       if(list!=null)
       {
           return DtoUtil.returnDataSuccess(list);
       }
        return DtoUtil.returnFail("数据访问失败","10002");
    }
    @Resource
    HotelServer biz;

   // method=RequestMethod.POST,produces = "application/json")

    @RequestMapping(value = "/hotel/queryhotcity/{type}",method=RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto GetCity(@PathVariable Integer type)
    {
        System.out.println(type);
        List<ItripAreaDic> pojo=biz.GetData(type);
        if(pojo!=null)
        {
           return   DtoUtil.returnDataSuccess(pojo);
        }
        return DtoUtil.returnFail("数据访问失败","10002");
    }
}
