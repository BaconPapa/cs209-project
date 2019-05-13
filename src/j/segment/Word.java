package j.segment;

import java.util.Objects;

public class Word {
    private String content;
    private String partOfSpeech;

    public Word(String content, String partOfSpeech) {
        this.content = content;
        this.partOfSpeech = partOfSpeech;
    }

    public String getContent() {
        return content;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return content.equals(word.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "Word{" +
                "content='" + content + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                '}';
    }
}
