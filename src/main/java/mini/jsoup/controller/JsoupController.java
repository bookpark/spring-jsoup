package mini.jsoup.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsoupController {
    //썸네일 링크
    @GetMapping("/api/thumbnail")
    public String thumbnail() {
        final String inflearnUrl = "https://www.inflearn.com/courses/it-programming";
        Connection conn = Jsoup.connect(inflearnUrl);
        // Buffer는 동기화, Builder는 동기화X
        StringBuilder sb = new StringBuilder();
        try {
            Document document = conn.get();
            Elements imgUrlElements = document.getElementsByClass("swiper-lazy");
            for (Element element : imgUrlElements) {
                sb.append(element.attr("abs:src")).append("<br />");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
