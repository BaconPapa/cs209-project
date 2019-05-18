package j.service;

import j.crawl.Crawler;
import j.model.Comment;
import j.segment.WordSegment;
import j.segment.WordSegmentFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * This class is used to combine word segment, crawl, statistic together to provide an uniform interface
 */
public class SegmentProcess implements Closeable {
    private ExecutorService threadPool;
    private WordSegment wordSegment;

    public SegmentProcess() {
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
    public List<Comment> crawlProduct(String productId, int pageCount) throws InterruptedException {
        ConcurrentLinkedQueue<List<Comment>> commentsQueue = new ConcurrentLinkedQueue<>();
        LinkedList<Comment> result = new LinkedList<>();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        SegmentProcess.CrawlRunner crawlRunner = new CrawlRunner(commentsQueue, productId, pageCount);
        SegmentProcess.SegmentRunner segmentRunner = new SegmentRunner(
                commentsQueue,
                result,
                wordSegment,
                countDownLatch
        );
        threadPool.execute(crawlRunner);
        threadPool.execute(segmentRunner);
        countDownLatch.await();
        return result;
    }

    /**
     * it must be called after the end of program
     */
    @Override
    public void close() {
        threadPool.shutdown();
    }

    private class SegmentRunner implements Runnable {
        private ConcurrentLinkedQueue<List<Comment>> commentsQueue;
        WordSegment wordSegment;
        CountDownLatch countDownLatch;
        LinkedList<Comment> resultList;

        SegmentRunner(
                ConcurrentLinkedQueue<List<Comment>> commentsQueue,
                LinkedList<Comment> resultList,
                WordSegment wordSegment,
                CountDownLatch countDownLatch) {
            this.commentsQueue = commentsQueue;
            this.countDownLatch = countDownLatch;
            this.resultList = resultList;
            this.wordSegment = wordSegment;
        }

        @Override
        public void run() {
            while (countDownLatch.getCount() > 0) {
                List<Comment> comments = commentsQueue.poll();
                if (comments == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                for (Comment comment : comments) {
                    List<String> words = wordSegment.wordSegment(comment.getContent()).stream().distinct().collect(Collectors.toList());
                    List<String> phrases = wordSegment.phraseSegment(comment.getContent()).stream().distinct().collect(Collectors.toList());
                    comment.setWords(words);
                    comment.setPhrases(phrases);
                    resultList.add(comment);
                }
                countDownLatch.countDown();
                System.out.printf("Current Progress: %d\n", countDownLatch.getCount());
            }
        }
    }

    private class CrawlRunner implements Runnable {
        private ConcurrentLinkedQueue<List<Comment>> commentsQueue;
        private String productId;
        private int pageCount;

        CrawlRunner(ConcurrentLinkedQueue<List<Comment>> commentsQueue, String productId, int pageCount) {
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
