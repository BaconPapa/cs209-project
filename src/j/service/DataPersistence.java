package j.service;

import j.model.Comment;
import j.model.CommodityEvaluate;
import j.model.FilterMode;
import j.model.WordFrequency;
import j.util.DateUtil;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DataPersistence {
    public void saveEvaluateData(CommodityEvaluate commodityEvaluate, String path) throws IOException {
        String objectString = Json.createObjectBuilder()
                .add("filterMode", commodityEvaluate.getFilterMode().toString())
                .add("comments", commentsToJsonArray(commodityEvaluate.getComments()))
                .add("wordFrequencies", frequencyToArray(commodityEvaluate.getWordFrequencies()))
                .add("score", commodityEvaluate.getScore())
                .build().toString();
        Files.writeString(
                new File(path).toPath(),
                objectString,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        );
    }

    public CommodityEvaluate loadEvaluateData(String path) throws IOException, ParseException {
        File file = new File(path);
        String content = Files.readString(file.toPath());
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        return new CommodityEvaluate(
                FilterMode.valueOf(jsonObject.getString("filterMode")),
                jsonArrayToComments(jsonObject.getJsonArray("comments")),
                jsonArrayToWordFrequencies(jsonObject.getJsonArray("wordFrequencies")),
                jsonObject.getInt("score")
        );
    }

    public void saveComments(List<Comment> comments, String path) throws IOException {
        File file = new File(path);
        String jsonArray = commentsToJsonArray(comments).toString();
        Files.writeString(
                file.toPath(),
                jsonArray,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        );
    }

    public List<Comment> readComments(String path) throws IOException, ParseException {
        File file = new File(path);
        String content = Files.readString(file.toPath());
        JsonArray jsonArray = Json.createReader(new StringReader(content)).readArray();
        return jsonArrayToComments(jsonArray);
    }

    private JsonArray commentsToJsonArray(List<Comment> comments) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Comment comment : comments) {
            JsonArray words = Json.createArrayBuilder(comment.getWords()).build();
            JsonArray phrases = Json.createArrayBuilder(comment.getPhrases()).build();
            JsonObject object = Json.createObjectBuilder()
                    .add("replyCount", comment.getReplyCount())
                    .add("userScore", comment.getUserScore())
                    .add("usefulVoteCount", comment.getUsefulVoteCount())
                    .add("referenceTime", DateUtil.dateToString(comment.getReferenceTime()))
                    .add("content", comment.getContent())
                    .add("words", words)
                    .add("phrases", phrases)
                    .build();
            arrayBuilder.add(object);
        }
        return arrayBuilder.build();
    }

    private JsonArray frequencyToArray(List<WordFrequency> wordFrequencies) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (WordFrequency frequency : wordFrequencies) {
            JsonObject object = Json.createObjectBuilder()
                    .add("content", frequency.getContent())
                    .add("frequency", frequency.getFrequency())
                    .add("weight", frequency.getWeight())
                    .build();
            arrayBuilder.add(object);
        }
        return arrayBuilder.build();
    }


    private List<Comment> jsonArrayToComments(JsonArray jsonArray) throws ParseException {
        List<Comment> comments = new LinkedList<>();
        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            List<String> words = jsonObject.getJsonArray("words").stream()
                    .map(JsonValue::toString)
                    .collect(toList());
            List<String> phrases = jsonObject.getJsonArray("phrases").stream()
                    .map(JsonValue::toString)
                    .collect(toList());
            Comment comment = new Comment(
                    jsonObject.getInt("replyCount"),
                    jsonObject.getInt("userScore"),
                    jsonObject.getInt("usefulVoteCount"),
                    DateUtil.stringToDate(jsonObject.getString("referenceTime")),
                    jsonObject.getString("content"),
                    words,
                    phrases
            );
            comments.add(comment);
        }
        return comments;
    }

    private List<WordFrequency> jsonArrayToWordFrequencies(JsonArray jsonArray) {
        List<WordFrequency> wordFrequencies = new LinkedList<>();
        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            WordFrequency wordFrequency = new WordFrequency(
                    jsonObject.getString("content"),
                    jsonObject.getJsonNumber("frequency").doubleValue(),
                    jsonObject.getInt("weight")
            );
            wordFrequencies.add(wordFrequency);
        }
        return wordFrequencies;
    }

}
