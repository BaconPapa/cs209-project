package j.model;

import java.util.List;

public class CommodityEvaluate {
    private FilterMode filterMode;
    private List<Comment> comments;
    private List<WordFrequency> wordFrequencies;
    private int score;

    public CommodityEvaluate(FilterMode filterMode, List<Comment> comments, List<WordFrequency> wordFrequencies, int score) {
        this(filterMode, comments, wordFrequencies);
        this.score = score;
    }

    public CommodityEvaluate(FilterMode filterMode, List<Comment> comments, List<WordFrequency> wordFrequencies) {
        this(comments, wordFrequencies);
        this.filterMode = filterMode;
    }

    public CommodityEvaluate(List<Comment> comments, List<WordFrequency> wordFrequencies) {
        this.comments = comments;
        this.filterMode = FilterMode.None;
        this.wordFrequencies = wordFrequencies;
        this.score = 0;
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<WordFrequency> getWordFrequencies() {
        return wordFrequencies;
    }

    public void setWordFrequencies(List<WordFrequency> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }

    @Override
    public String toString() {
        return "CommodityEvaluate{" +
                "filterMode=" + filterMode +
                ", comments=" + comments +
                '}';
    }
}
