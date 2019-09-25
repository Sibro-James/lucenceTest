package com.sibro.lucence.thread;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sibro.lucence.dto.BookDTO;
import com.sibro.lucence.mapper.BookMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Service
public class CreateIndexTestThread implements Runnable {
    @Autowired
    private BookMapper bookMapper;

    public void run(){
        try {
            testCreateIndex();
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
            doc.add(new TextField("id", String.valueOf(bk.getId()), Store.YES));// Store.YES:表示存储到文档域中
            doc.add(new TextField("name", bk.getName(), Store.YES));
            doc.add(new TextField("price", String.valueOf(bk.getPrice()), Store.YES));
            doc.add(new TextField("pic", bk.getPic(), Store.YES));
            doc.add(new TextField("desc", bk.getDesc(), Store.YES));

            // 把Document放到list中
            documents.add(doc);
        }

        //    3. 创建分析器（分词器）
        //Analyzer analyzer = new StandardAnalyzer();
        //中文  IK
        Analyzer analyzer = new IKAnalyzer();
        //    4. 创建IndexWriterConfig配置信息类
        IndexWriterConfig config = new IndexWriterConfig( analyzer);

        //    5. 创建Directory对象，声明索引库存储位置
        Directory directory = FSDirectory.open(Paths.get("D:\\lucence"));

        //    6. 创建IndexWriter写入对象
        IndexWriter writer = new IndexWriter(directory, config);

        //    7. 把Document写入到索引库中
        for (Document doc : documents) {
            writer.addDocument(doc);
        }

        //    8. 释放资源
        writer.close();
    }
}