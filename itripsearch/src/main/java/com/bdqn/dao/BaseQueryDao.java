package com.bdqn.dao;

import cn.itrip.common.Page;
import com.bdqn.entity.ItripHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.List;

public class BaseQueryDao<T> {
    public static String url = "http://localhost:8094/solr-4.9.1/hotel-core1/";

    HttpSolrClient httpSolrClient;
    public BaseQueryDao()
    {
        //初始化HttpSolrClient
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrClient.setConnectionTimeout(500); // 建立连接的最长时间
    }


    //封装分页solr方法
    public Page<T> GetPageList_1(SolrQuery query,int Pageindex,int PageSize,Class T)
    {
        query.setSort("id", SolrQuery.ORDER.desc);

        query.setStart((Pageindex-1)*PageSize);
        //一页显示多少条
        query.setRows(PageSize);
        QueryResponse queryResponse = null;

        try {
            queryResponse = httpSolrClient.query(query);
            List<T> list = queryResponse.getBeans(T);


            //读取solr中的所有数据的一个结果
            SolrDocumentList docs = queryResponse.getResults();

            Page<T> listPage=new Page<>(Pageindex,PageSize,new Long(docs.getNumFound()).intValue());
            listPage.setRows(list);
            return listPage;
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {

        }

    }

    //封装返回数据的方法
    public List<T> GetPageList(SolrQuery query,int Count,Class T)
    {
        query.setSort("id", SolrQuery.ORDER.desc);
        query.setStart(0);
        //一页显示多少条
        query.setRows(Count);
        QueryResponse queryResponse = null;

        try {
            queryResponse = httpSolrClient.query(query);
            List<T> list = queryResponse.getBeans(T);
            return list;
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {

        }


    }

}
