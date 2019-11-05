package com.pres.lucene;

import com.pres.ik.IKAnalyzer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.surround.parser.ParseException;

import org.apache.lucene.queryparser.surround.query.SrndQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LuceneApplicationTests {

    /**
     * 加索引
     * @author dora 
     * @return   
     * @date 2019/10/30
    **/
    @Test
    @SneakyThrows({IOException.class})
   public void contextLoads() {
        // 获取目录
        Directory directory = FSDirectory.open(Paths.get("F:\\WechatFile\\lucene"));

        // 标准解析器
        Analyzer analyzer = new IKAnalyzer();

        // 创建分词器 并指定分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 创建index
        IndexWriter indexWriter = new IndexWriter(directory,config);

        // 创建document
        addDocument("女装男装内衣","T恤衬衫蕾丝衫/雪纺衫卫衣绒衫连帽卫衣毛针织衫毛衣开衫毛衣套头针织衫",indexWriter);
        addDocument("鞋靴 / 箱包 / 配件","春上新\n" +
                "低帮鞋\n" +
                "单鞋\n" +
                "运动风\n" +
                "一脚蹬\n" +
                "靴子\n" +
                "高跟鞋\n" +
                "厚底鞋\n" +
                "平底鞋\n" +
                "小白鞋\n" +
                "玛丽珍鞋\n" +
                "圆头鞋\n" +
                "中跟鞋\n" +
                "尖头鞋\n" +
                "异形跟\n" +
                "方头鞋\n" +
                "水晶透明鞋\n" +
                "穆勒鞋\n" +
                "内增高\n" +
                "毛毛鞋\n" +
                "当季热",indexWriter);
        addDocument("童装玩具 / 孕产 / 用品","母婴用品\n" +
                "纸尿裤 新生儿 婴儿推车 奶瓶 婴儿床 睡袋 餐具 安全座椅 湿巾 体温计 沐浴露 润肤乳\n" +
                "时髦孕妈\n" +
                "孕妇上装 连衣裙 月子服 哺乳文胸 待产包 束腹带 孕期洗护 吸奶器 孕妇裤 防辐射 营养食品\n" +
                "潮童鞋服\n" +
                "儿童套装 外套 裤子 裙子 亲子装 牛仔裤 连体衣 T恤 内衣裤 演出服\n" +
                "童鞋上新\n" +
                "凉鞋 运动鞋 皮鞋 公主鞋 帆布鞋 学步鞋 雨鞋 靴子 亲子鞋 母女鞋 棉拖鞋\n" +
                "潮流玩具\n" +
                "童书 户外玩具 积木 早教 电动车 儿童自行车 爬行垫 机器人 亲子玩具",indexWriter);
        //关闭流
        indexWriter.close();


    }
    
    @SneakyThrows
    void addDocument(String title ,String desc,IndexWriter indexWriter){
        // 创建Document
        Document document=new Document();
        document.add(new TextField("title",title, Field.Store.YES));
        document.add(new TextField("desc",desc,Field.Store.YES));
        indexWriter.addDocument(document);

    }
    /**
     * 查询
     * @author dora 
     * @return   
     * @date 2019/10/30
    **/
    @SneakyThrows({org.apache.lucene.queryparser.classic.ParseException.class,IOException.class})
    @Test
    public void search() {
        // query
        String queryStr="亲子卫";

        Analyzer  analyzer=new IKAnalyzer();
        Query parse = new QueryParser("desc",analyzer).parse(queryStr);

        int hitsPerPage = 10;
        // 创建索引所在的文件夹
        Directory directory = FSDirectory.open(Paths.get("F:\\WechatFile\\lucene"));

        // 创建indexReader
        DirectoryReader reader = DirectoryReader.open(directory);

        // 根据indexReader创建indexSearch
        IndexSearcher indexSearcher = new IndexSearcher(reader);


        TopDocs search = indexSearcher.search(parse, 10);
        ScoreDoc[] hits =search.scoreDocs;

        System.out.println("hits.length = " + hits.length);

        for (int i = 0; i < hits.length; i++) {
            int doc = hits[i].doc;
            Document doc1 = indexSearcher.doc(doc);
            System.out.println((i + 1) + ". " + doc1.get("desc") + "\t" + doc1.get("title"));
        }

        reader.close();
    }

    /**
     * 分词
     * @author dora 
     * @return   
     * @date 2019/10/30
    **/
    @Test
    @SneakyThrows({IOException.class})
    public void cutWords(){
        Analyzer analyzer=new IKAnalyzer();
        String text = "厉害了我的国一经播出，受到各方好评，强烈激发了国人的爱国之情、自豪感！";
        TokenStream tokenStream = analyzer.tokenStream("content", text);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println("分词--"+charTermAttribute.toString());
        }
        tokenStream.end();
        tokenStream.close();
        analyzer.close();
    }






}
