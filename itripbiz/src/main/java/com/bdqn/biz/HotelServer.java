package com.bdqn.biz;


import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.pojo.ItripAreaDic;
import cn.itrip.pojo.ItripLabelDic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HotelServer {

    @Resource
    ItripAreaDicMapper dao;

    @Resource
    ItripLabelDicMapper dicdao;

    //根据类型获取国内国外的热门城市
    public List<ItripAreaDic> GetData(int type)
    {
       return dao.getItripAreaDicByType(type);
    }

    //读取特色酒店列表
    public List<ItripLabelDic> GetDataFrea()
    {
        return dicdao.getFirstData();
    }


}
