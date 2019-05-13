package j.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Crawler {
    public List<String> crawlComment(String productId, int pageIndex) throws IOException {
        HashMap<String, String> headers = createHeader(productId);
        String url = String.format("https://sclub.jd.com/comment/productPageComments.action" +
                "?callback=fetchJSON_comment98vv1275&productId=%s&score=0&sortType=5&page=%d&pageSize=10&isShadowSku=0&rid=0&fold=1",
                productId, pageIndex);
        Document document = Jsoup.connect(url)
                .headers(headers)
                .get();
        String[] body = document.body().text().split("\\(");
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 1; i < body.length; i++) {
            contentBuilder.append(body[i]);
        }
        String content = contentBuilder.toString();
        content = content.substring(0, content.length() - 2);
        return parseComment(content);
    }

    private HashMap<String, String> createHeader(String productId) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Referer", String.format("https://item.jd.com/%s.html", productId));
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
        return header;
    }

    private List<String> parseComment(String jsonString) {
        LinkedList<String> resultList = new LinkedList<>();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        try {
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray comments = jsonObject.getJsonArray("comments");
            for (JsonValue comment : comments) {
                resultList.add(comment.asJsonObject().getString("content"));
            }
        } catch (JsonParsingException e) {
            System.out.printf("Jason parsing error for json string:\n%s\n", jsonString);
        }

        return new ArrayList<>(resultList);
    }
}
