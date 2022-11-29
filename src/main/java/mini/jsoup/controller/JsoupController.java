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
            Elements course_title = document.getElementsByClass("course_title");
            for (Element element : imgUrlElements) {
                sb.append(element.attr("abs:src")).append("<br />").append(course_title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @GetMapping("/api/title")
    public String title() {
        final String inflearnUrl = "https://www.inflearn.com/courses/it-programming";
        Connection conn = Jsoup.connect(inflearnUrl);
        // Buffer는 동기화, Builder는 동기화X
        StringBuilder sb = new StringBuilder();
        try {
            Document document = conn.get();
            Elements course_title = document.select("div.card-content>div.course_title");
            for (Element element : course_title) {
                sb.append(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @GetMapping("/api/price")
    public String price() {
        final String inflearnUrl = "https://www.inflearn.com/courses/it-programming";
        Connection conn = Jsoup.connect(inflearnUrl);
        // Buffer는 동기화, Builder는 동기화X
        StringBuilder sb = new StringBuilder();
        try {
            Document document = conn.get();
            Elements price = document.select("div.price");
            for (Element element : price) {
                String rawPrice = element.text();
                String realPrice = getRealPrice(rawPrice);
                String salePrice = getSalePrice(rawPrice);

                int nRealPrice = toInt(realPrice);
                int nSalePrice = toInt(salePrice);
                sb.append("가격: ").append(nRealPrice);

                if (nRealPrice != nSalePrice) {
                    sb.append("&nbsp;할인가격: ").append(nSalePrice);
                }
                sb.append("<br />");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getRealPrice(String price) {
        return price.split(" ")[0];
    }

    private String getSalePrice(String price) {
        String[] prices = price.split(" ");
        return prices.length == 1 ? prices[0] : prices[1];
    }

    private int toInt(String str) {
        str = str.replaceAll("₩", "");
        str = str.replaceAll(",", "");
        return Integer.parseInt(str);
    }
}
