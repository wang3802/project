package com.bdqn.controller;

import cn.itrip.common.Page;
import cn.itrip.pojo.Dto;
import cn.itrip.pojo.DtoUtil;
import cn.itrip.pojo.SearchHotCityVO;
import com.bdqn.biz.SearchBiz;
import com.bdqn.entity.ItripHotelVO;
import com.bdqn.entity.SearchHotelVO;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api")
public class SearchHotel {
    @RequestMapping(value = "/hotellist/searchItripHotelListByHotCity",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto GetSearchHotel(@RequestBody SearchHotCityVO vo)
    {
        SearchBiz biz=new SearchBiz();
        List<ItripHotelVO> list=biz.GetList(vo.getCityId(),vo.getCount());
        return DtoUtil.returnDataSuccess(list);
    }
    @RequestMapping(value = "/hotellist/searchItripHotelPage",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto GetSearchHote2(@RequestBody SearchHotelVO vo)
    {
        SearchBiz biz=new SearchBiz();
        if(vo.getPageNo()==null)
        {            vo.setPageNo(1);

        }
        Page<ItripHotelVO> list=biz.GetList_1(vo,vo.getPageNo(),6);
        return DtoUtil.returnDataSuccess(list);
    }
}
