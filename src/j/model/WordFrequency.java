package j.model;

public class WordFrequency {
    private String content;
    private double frequency;
    private int weight;

    public WordFrequency(String content, double frequency, int weight) {
        this(content, frequency);
        this.weight = weight;
    }

    public WordFrequency(String content, double frequency) {
        this.content = content;
        this.frequency = frequency;
        this.weight = 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WordFrequency{" +
                "content='" + content + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
