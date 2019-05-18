package j.segment;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Constituent;
import edu.stanford.nlp.trees.LabeledScoredConstituentFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import j.config.SegmentConfig;

import java.io.IOException;
import java.util.*;

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
    public List<String> wordSegment(String text) {
        ArrayList<String> words = new ArrayList<>();
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        nlpPipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (!pos.equals(SegmentConfig.POS_PUNCTUATION)) {
                    words.add(word);
                }
            }
        }
        return words;
    }
    public List<String> phraseSegment(String text) {
        ArrayList<String> phrases = new ArrayList<>();
        Annotation annotation = new Annotation(text);
        nlpPipeline.annotate(annotation);
        Tree tree = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeCoreAnnotations.TreeAnnotation.class);
        Set<Constituent> treeConstituents = tree.constituents(new LabeledScoredConstituentFactory());
        for (Constituent constituent : treeConstituents) {
            if (constituent.label() != null &&
                    (constituent.label().toString().equals(SegmentConfig.VERB_PHRASE) || constituent.label().toString().equals(SegmentConfig.NOUN_PHRASE)) &&
                    constituent.end() + 1 - constituent.start() > 1
            ) {
                List<Tree> leaves = tree.getLeaves().subList(constituent.start(), constituent.end() + 1);
                StringBuilder builder = new StringBuilder();
                for (Tree leaf : leaves) {
                    builder.append(leaf.value());
                }
                phrases.add(builder.toString());
            }
        }
        return phrases;
    }
}
