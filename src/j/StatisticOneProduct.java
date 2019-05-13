package j;

import j.statistic.StatisticModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * 统计例程
 */
public class StatisticOneProduct {
    public static void main(String[] args) throws InterruptedException, IOException {
        String[] productIds = {"100002539302", "100000177748", "31544565956", "32942468226", "32943757458"};
        int pageCount = 50;
        StatisticProcess statisticProcess = new StatisticProcess();
        // 获取信息前先进行统计
        statisticProcess.config();
        for (String productId : productIds) {
            // 爬取信息
            List<StatisticModel> result = statisticProcess.statisticProduct(productId, pageCount);

            //将信息写入csv文件
            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append("word,POS,count\n");
            for (StatisticModel wordIntegerEntry : result) {
                resultBuilder.append(wordIntegerEntry.getContent());
                resultBuilder.append(',');
                resultBuilder.append(wordIntegerEntry.getPartOfSpeech());
                resultBuilder.append(',');
                resultBuilder.append(wordIntegerEntry.getCount());
                resultBuilder.append('\n');
            }
            File file = new File(String.format("./statistic/%s_%d.csv", productId, pageCount));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.writeString(
                    file.toPath(),
                    resultBuilder.toString(), StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        }
        statisticProcess.close();
    }
}
