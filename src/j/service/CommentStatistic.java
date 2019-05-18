package j.service;

import j.config.StatisticConfig;
import j.model.Comment;
import j.model.CommodityEvaluate;
import j.model.WordFrequency;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommentStatistic {
    /**
     * Return top n frequency for comments word
     * @param comments comments to statistic
     * @param n the number of comment to be returned
     * @return top n frequency for comments word
     */
    public List<WordFrequency> getWordFrequency(List<Comment> comments, int n) {
        return statisticFrequency(comments, n, Comment::getWords);
    }

    /**
     * Return top n frequency for comments phrase
     * @param comments comments to statistic
     * @param n the number of comment to be returned
     * @return top n frequency for comments phrase
     */
    public List<WordFrequency> getPhraseFrequency(List<Comment> comments, int n) {
        return statisticFrequency(comments, n, Comment::getPhrases);
    }

    public List<Comment> filterByReplyCount(List<Comment> comments, int replyCount) {
        return comments.stream()
                .filter(comment -> comment.getReplyCount() >= replyCount)
                .collect(Collectors.toList());
    }

    public List<Comment> filterByDate(List<Comment> comments, long timeInterval) {
        Date date = new Date();
        long earliestTime = date.getTime() - timeInterval;
        return comments.stream()
                .filter(comment -> comment.getReferenceTime().getTime() >= earliestTime)
                .collect(Collectors.toList());
    }

    public List<Comment> filterByLike(List<Comment> comments, int likeCount) {
        return comments.stream()
                .filter(comment -> comment.getUsefulVoteCount() >= likeCount)
                .collect(Collectors.toList());
    }

    public long evaluateComment(Comment comment, List<WordFrequency> wordFrequencies) {
        long score = comment.getUserScore() * StatisticConfig.USER_SCORE_WEIGHT +
                comment.getUsefulVoteCount() * StatisticConfig.LIKE_COUNT_WEIGHT +
                comment.getReplyCount() * StatisticConfig.REPLY_COUNT_WEIGHT;
        List<String> phrases = comment.getPhrases();
        for (WordFrequency wordFrequency : wordFrequencies) {
            if (phrases.contains(wordFrequency.getContent())) {
                score += wordFrequency.getWeight();
            }
        }
        return score;
    }

    public long calculateScore(CommodityEvaluate evaluate) {
        long wholeScore = 0;
        List<Comment> comments = evaluate.getComments();
        int commentCount = comments.size();
        for (Comment comment : comments) {
            wholeScore += evaluateComment(comment, evaluate.getWordFrequencies());
        }
        return wholeScore / commentCount;
    }

    private List<WordFrequency> statisticFrequency(List<Comment> comments, int n, Function<Comment, List<String>> commentListFunction) {
        int wholeNum = comments.size();
        HashMap<String, Integer> wordMap = new HashMap<>();
        List<WordFrequency> wordFrequencies = new LinkedList<>();
        for (Comment comment : comments) {
            for (String word : commentListFunction.apply(comment)) {
                if (!wordMap.containsKey(word)) {
                    wordMap.put(word, 0);
                }
                int times = wordMap.get(word) + 1;
                wordMap.put(word, times);
            }
        }
        for (Map.Entry<String, Integer> stringIntegerEntry : wordMap.entrySet()) {
            wordFrequencies.add(
                    new WordFrequency(
                            stringIntegerEntry.getKey(),
                            (double) stringIntegerEntry.getValue() / (double) wholeNum)
            );
        }
        wordFrequencies.sort(Comparator.comparing(wordFrequency -> -wordFrequency.getFrequency()));
        return wordFrequencies.subList(0, n);
    }


}
