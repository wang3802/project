package com.bdqn.biz;

import cn.itrip.common.Page;
import com.bdqn.dao.BaseQueryDao;
import com.bdqn.entity.ItripHotelVO;
import com.bdqn.entity.SearchHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.QueryEval;
import java.util.List;

public class SearchBiz {

    public List<ItripHotelVO> GetList(int city,int count)
    {
        BaseQueryDao<ItripHotelVO> dao=new BaseQueryDao();
        SolrQuery query=new SolrQuery("*:*");
        query.addFilterQuery("cityId:"+city);
        return dao.GetPageList(query,count,ItripHotelVO.class);
    }

    public Page<ItripHotelVO> GetList_1(SearchHotelVO vo,int pageindex,int pagesize)
    {
        BaseQueryDao<ItripHotelVO> dao=new BaseQueryDao();
        SolrQuery query=new SolrQuery("*:*");
      /*  StringBuilder str=new StringBuilder();
        str.append("*:*");*/
       if(vo.getDestination()!=null)
       {
           query.addFilterQuery(" destination:" + vo.getDestination());
       }
        if(vo.getKeywords()!=null)
        {
            query.addFilterQuery(" keyword:"+vo.getKeywords());
        }
        if(vo.getMinPrice()!=null)
        {
            query.addFilterQuery(" minPrice:"+"["+ vo.getMinPrice()+" TO *]");
        }
        if (vo.getMaxPrice()!=null)
        {
            query.addFilterQuery(" maxPrice:"+"[* TO "+vo.getMaxPrice()+"]");
        }
        if(vo.getFeatureIds()!=null&&vo.getFeatureIds()!="") {
            //   ,17,115,116,117,
            String[] substr = vo.getFeatureIds().split(",");

            //    featureIds:[*17*]
            for (String i : substr) {
                query.addFilterQuery(" featureIds:*," + i + ",*");
            }
        }
    /*    tradingAreaIds:*,3619,**/
        if(vo.getTradeAreaIds()!=null&&vo.getTradeAreaIds()!="")
        {
            //   ,17,115,116,117,
            String[] substr = vo.getTradeAreaIds().split(",");


            for (String i : substr) {
                query.addFilterQuery(" tradingAreaIds:*," + i + ",*");
            }
        }
        if(vo.getAscSort()!=null&&vo.getAscSort()!="")
        {
            query.setSort(vo.getAscSort(),SolrQuery.ORDER.asc);
        }
        return dao.GetPageList_1(query,pageindex,pagesize,ItripHotelVO.class);
    }
}
