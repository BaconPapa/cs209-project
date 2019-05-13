package j;

import j.crawl.Crawler;
import j.segment.Word;
import j.segment.WordSegment;
import j.segment.WordSegmentFactory;
import j.statistic.StatisticModel;
import j.statistic.WordStatistic;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is used to combine word segment, crawl, statistic together to provide an uniform interface
 */
public class StatisticProcess implements Closeable {
    private ExecutorService threadPool;
    private WordSegment wordSegment;

    public StatisticProcess() {
        threadPool = Executors.newFixedThreadPool(2);
        wordSegment = WordSegmentFactory.createSegmentor();
    }

    /**
     * This method should be called before statistic one product
     */
    public void config() {
        wordSegment.config();
    }

    /**
     * Crawling the commands and statistic important word
     * @param productId id of product, can be checked from jd's web page
     * @param pageCount count of page to be crawled
     * @return result model
     * @throws InterruptedException thrown by unexpected interrupt of main thread
     */
    public List<StatisticModel> statisticProduct(String productId, int pageCount) throws InterruptedException {
        ConcurrentLinkedQueue<List<String>> commentsQueue = new ConcurrentLinkedQueue<>();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        WordStatistic wordStatistic = new WordStatistic();
        StatisticProcess.CrawlRunner crawlRunner = new StatisticProcess.CrawlRunner(commentsQueue, productId, pageCount);
        StatisticProcess.SegmentRunner segmentRunner = new StatisticProcess.SegmentRunner(commentsQueue, wordSegment, wordStatistic, countDownLatch);
        threadPool.execute(crawlRunner);
        threadPool.execute(segmentRunner);
        countDownLatch.await();
        return wordStatistic.getStatisticResult();
    }

    /**
     * it must be called after the end of program
     */
    @Override
    public void close() {
        threadPool.shutdown();
    }

    private class SegmentRunner implements Runnable {
        private ConcurrentLinkedQueue<List<String>> commentsQueue;
        WordStatistic wordStatistic;
        WordSegment wordSegment;
        CountDownLatch countDownLatch;

        SegmentRunner(ConcurrentLinkedQueue<List<String>> commentsQueue, WordSegment wordSegment, WordStatistic wordStatistic, CountDownLatch countDownLatch) {
            this.commentsQueue = commentsQueue;
            this.wordStatistic = wordStatistic;
            this.countDownLatch = countDownLatch;
            this.wordSegment = wordSegment;
        }

        @Override
        public void run() {
            while (countDownLatch.getCount() > 0) {
                List<String> comments = commentsQueue.poll();
                if (comments == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                for (String comment : comments) {
                    List<Word> words = wordSegment.wordSegment(comment);
                    wordStatistic.addWords(words);
                }
                countDownLatch.countDown();
                System.out.printf("Current Progress: %d\n", countDownLatch.getCount());
            }
        }
    }

    private class CrawlRunner implements Runnable {
        private ConcurrentLinkedQueue<List<String>> commentsQueue;
        private String productId;
        private int pageCount;

        CrawlRunner(ConcurrentLinkedQueue<List<String>> commentsQueue, String productId, int pageCount) {
            this.commentsQueue = commentsQueue;
            this.productId = productId;
            this.pageCount = pageCount;
        }

        @Override
        public void run() {
            Crawler crawler = new Crawler();
            for (int i = 0; i <= pageCount; i++) {
                try {
                    commentsQueue.add(crawler.crawlComment(productId, i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
