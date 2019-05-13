package j.statistic;

import j.segment.Word;

import java.util.Map;

public class StatisticModel {
    private String content;
    private String partOfSpeech;
    private Integer count;

    public StatisticModel(String content, String partOfSpeech, Integer count) {
        this.content = content;
        this.partOfSpeech = partOfSpeech;
        this.count = count;
    }

    public StatisticModel(Map.Entry<Word, Integer> wordIntegerEntry) {
        this.content = wordIntegerEntry.getKey().getContent();
        this.partOfSpeech = wordIntegerEntry.getKey().getPartOfSpeech();
        this.count = wordIntegerEntry.getValue();
    }

    public String getContent() {
        return content;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "StatisticModel{" +
                "content='" + content + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", count=" + count +
                '}';
    }
}
