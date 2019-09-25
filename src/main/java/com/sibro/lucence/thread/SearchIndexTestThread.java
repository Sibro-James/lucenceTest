package com.sibro.lucence.thread;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;

@Service
public class SearchIndexTestThread implements Runnable  {
    public void run(){
        try {
            serachIndex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查
    @Test
    public void serachIndex() throws Exception{
        //创建分词器   必须和检索时的分析器一致
//        Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
        QueryParser queryParser = new QueryParser("name", analyzer);

        // 1. 创建Query搜索对象
        Query query = queryParser.parse("name:java");

        // 2. 创建Directory流对象,声明索引库位置
        Directory directory = FSDirectory.open(Paths.get("D:\\lucence"));

        // 3. 创建索引读取对象IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);

        // 4. 创建索引搜索对象IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
        // 第一个参数：搜索对象，第二个参数：返回的数据条数，指定查询结果最顶部的n条数据返回
        TopDocs topDocs  = indexSearcher.search(query, 10);
        System.out.println("查询到的数据总条数是：" + topDocs.totalHits);
        //获得结果集
        ScoreDoc[] docs = topDocs.scoreDocs;

        // 6. 解析结果集
        for (ScoreDoc scoreDoc : docs) {
            //获得文档
            int docID = scoreDoc.doc;
            Document doc = indexSearcher.doc(docID);

            System.out.println("docID:"+docID);
            System.out.println("bookid:"+doc.get("id"));
            System.out.println("pic:"+doc.get("pic"));
            System.out.println("name:"+doc.get("name"));
            System.out.println("desc:"+doc.get("desc"));
            System.out.println("price:"+doc.get("price"));
        }

        // 7. 释放资源
        indexReader.close();
    }
}
