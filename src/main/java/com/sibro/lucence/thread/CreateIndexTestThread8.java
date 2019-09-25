package com.sibro.lucence.thread;

import com.sibro.lucence.dto.BookDTO;
import com.sibro.lucence.mapper.BookMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateIndexTestThread8 implements Runnable  {
    @Autowired
    private BookMapper bookMapper;

    public void run(){
        try {
            testCreateIndex();
            serachIndex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //分词
    @Test
    public void testCreateIndex() throws Exception{
        //    1. 采集数据
        List<BookDTO> listBook = bookMapper.queryBookList();

        //    2. 创建Document文档对象
        List<Document> documents = new ArrayList<>();
        for (BookDTO bk : listBook) {

            Document doc = new Document();
            doc.add(new TextField("id", String.valueOf(bk.getId()), Field.Store.YES));// Store.YES:表示存储到文档域中
            doc.add(new TextField("name", bk.getName(), Field.Store.YES));
            doc.add(new TextField("price", String.valueOf(bk.getPrice()), Field.Store.YES));
            doc.add(new TextField("pic", bk.getPic(), Field.Store.YES));
            doc.add(new TextField("desc", bk.getDesc(), Field.Store.YES));

            // 把Document放到list中
            documents.add(doc);
        }

        //    3. 创建分析器（分词器）
        //Analyzer analyzer = new StandardAnalyzer();
        //中文  IK
//        Analyzer analyzer = new IKAnalyzer();
        Analyzer analyzer = new StandardAnalyzer();
        //    4. 创建IndexWriterConfig配置信息类
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //    5. 创建Directory对象，声明索引库存储位置
        Directory directory = FSDirectory.open(new File("D:\\lucence"));

        //    6. 创建IndexWriter写入对象
        IndexWriter writer = new IndexWriter(directory, config);

        //    7. 把Document写入到索引库中
        for (Document doc : documents) {
            writer.addDocument(doc);
        }

        //    8. 释放资源
        writer.close();
    }


    //查
    @Test
    public void serachIndex() throws Exception{
        //创建分词器   必须和检索时的分析器一致
        Analyzer analyzer = new StandardAnalyzer();
//        Analyzer analyzer = new IKAnalyzer();
        // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
        QueryParser queryParser = new QueryParser("name", analyzer);

        // 1. 创建Query搜索对象
        Query query = queryParser.parse("name:java");

        // 2. 创建Directory流对象,声明索引库位置
        Directory directory = FSDirectory.open(new File("D:\\lucence"));

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
