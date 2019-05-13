package j.statistic;

import j.segment.Word;

import java.util.*;
import java.util.stream.Collectors;

public class WordStatistic {
    private HashMap<Word, Integer> statisticMap;
    public WordStatistic() {
        statisticMap = new HashMap<>();
    }
    public void addWords(List<Word> words) {
        for (Word word : words) {
            if (!statisticMap.containsKey(word)) {
                statisticMap.put(word, 0);
            }
            int count = statisticMap.get(word);
            statisticMap.put(word, count + 1);
        }
    }

    public List<StatisticModel> getStatisticResult() {
        return new ArrayList<>(statisticMap.entrySet()).stream()
                .sorted(Comparator.comparing(wordIntegerEntry -> -wordIntegerEntry.getValue()))
                .map(StatisticModel::new)
                .collect(Collectors.toList());
    }
}
