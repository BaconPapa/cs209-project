package j.model;

import java.util.Date;
import java.util.List;

public class Comment {
    /**
     * count of reply for this comment
     */
    private int replyCount;
    /**
     * User score
     */
    private int userScore;
    /**
     * Likes count
     */
    private int usefulVoteCount;
    /**
     * commit time
     */
    private Date referenceTime;
    /**
     * Comment content
     */
    private String content;
    /**
     * used to statistic words
     */
    private List<String> words;
    /**
     * used to statistic final score for comments
     */
    private List<String> phrases;

    public Comment(int replyCount, int userScore, int usefulVoteCount, Date referenceTime, String content) {
        this.replyCount = replyCount;
        this.userScore = userScore;
        this.usefulVoteCount = usefulVoteCount;
        this.referenceTime = referenceTime;
        this.content = content;
    }

    public Comment(int replyCount, int userScore, int usefulVoteCount, Date referenceTime, String content, List<String> words, List<String> phrases) {
        this.replyCount = replyCount;
        this.userScore = userScore;
        this.usefulVoteCount = usefulVoteCount;
        this.referenceTime = referenceTime;
        this.content = content;
        this.words = words;
        this.phrases = phrases;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int getUserScore() {
        return userScore;
    }

    public int getUsefulVoteCount() {
        return usefulVoteCount;
    }

    public Date getReferenceTime() {
        return referenceTime;
    }

    public String getContent() {
        return content;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getPhrases() {
        return phrases;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public void setUsefulVoteCount(int usefulVoteCount) {
        this.usefulVoteCount = usefulVoteCount;
    }

    public void setReferenceTime(Date referenceTime) {
        this.referenceTime = referenceTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "replyCount=" + replyCount +
                ", userScore=" + userScore +
                ", usefulVoteCount=" + usefulVoteCount +
                ", referenceTime=" + referenceTime +
                ", content='" + content + '\'' +
                ", words=" + words +
                ", phrases=" + phrases +
                '}';
    }
}
