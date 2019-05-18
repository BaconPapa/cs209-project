package j;

import j.model.CommodityEvaluate;
import j.service.CommentStatistic;
import j.service.DataPersistence;

import java.io.IOException;
import java.text.ParseException;

public class StatisticProduct {
    public static void main(String[] args) throws IOException, ParseException {
        DataPersistence dataPersistence = new DataPersistence();
        CommentStatistic commentStatistic = new CommentStatistic();
        CommodityEvaluate comments = dataPersistence.loadEvaluateData("comments.json");
        System.out.println(commentStatistic.calculateScore(comments));
    }
}
