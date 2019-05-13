package j.segment;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class WordSegment {
    private StanfordCoreNLP nlpPipeline;
    WordSegment() {}
    public void config() {
        Properties props = new Properties();
        try {
            props.load(Objects.requireNonNull(WordSegment.class.getClassLoader().getResourceAsStream("StanfordCoreNLP-chinese.properties")));
        } catch (IOException e) {
            System.out.println("NLP init error");
            System.exit(0);
        }
        nlpPipeline = new StanfordCoreNLP(props);
    }
    public List<Word> wordSegment(String text) {
        ArrayList<Word> words = new ArrayList<>();
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        nlpPipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (!pos.equals(SegmentConfig.POS_PUNCTUATION)) {
                    words.add(new Word(word, pos));
                }
            }
        }
        return words;
    }
}
