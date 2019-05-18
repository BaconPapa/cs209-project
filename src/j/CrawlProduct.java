package j;

import j.model.Comment;
import j.model.CommodityEvaluate;
import j.model.WordFrequency;
import j.service.CommentStatistic;
import j.service.DataPersistence;
import j.service.SegmentProcess;

import java.io.IOException;
import java.util.List;

/**
 * 统计例程
 */
public class CrawlProduct {
    public static void main(String[] args) throws InterruptedException, IOException {
        String[] productIds = {"100002539302"};
        int pageCount = 50;
        SegmentProcess segmentProcess = new SegmentProcess();
        DataPersistence dataPersistence = new DataPersistence();
        CommentStatistic commentStatistic = new CommentStatistic();
        // 获取信息前先进行统计
        segmentProcess.config();
        for (String productId : productIds) {
            // 爬取信息
            List<Comment> result = segmentProcess.crawlProduct(productId, pageCount);
            List<WordFrequency> wordFrequencies = commentStatistic.getPhraseFrequency(result, 10);
            CommodityEvaluate commodityEvaluate = new CommodityEvaluate(result, wordFrequencies);
            dataPersistence.saveEvaluateData(commodityEvaluate, "comments.json");
        }
        segmentProcess.close();
    }
}
