package mini.jsoup.controller;

import lombok.RequiredArgsConstructor;
import mini.jsoup.domain.Course;
import mini.jsoup.repository.CourseRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class InflearnController {

    private final CourseRepository courseRepository;

    final String inflearnUrl = "https://www.inflearn.com/courses/it-programming?order=seq&page=";
    private final int FIRST_PAGE_INDEX = 1;
    private final int LAST_PAGE_INDEX = 3;


    @GetMapping("/api/inflearn")
    public String all() {

        StringBuilder sb = new StringBuilder();
        try {
            for (int i = FIRST_PAGE_INDEX; i <= LAST_PAGE_INDEX; i++) {
                String url = inflearnUrl + i;
                Connection conn = Jsoup.connect(url);
                Document doc = conn.get();

                Elements images = doc.getElementsByClass("swiper-lazy");
                Elements titles = doc.select("div.card-content>div.course_title");
                Elements prices = doc.select("div.price");
                Elements instructors = doc.select("div.instructor");
                Elements descriptions = doc.select("p.course_description");
                Elements skills = doc.select("div.course_skills>span");
                Elements urls = doc.select("a.course_card_front");


                for (int j = 0; j < images.size(); j++) {
                    try {
                        String image = images.get(j).attr("abs:src");
                        String title = titles.get(j).text();
                        String price = prices.get(j).text();
                        int realPrice = 0, salePrice = 0;
                        if (!price.equals("무료")) {
                            realPrice = toInt(getRealPrice(price).replace("\\₩", ""));
                            salePrice = toInt(getSalePrice(price).replace("\\₩", ""));
                        }
                        String instructor = instructors.get(j).text();
                        String description = descriptions.get(j).text();
                        String skill = skills.get(j).text();

                        String innerUrl = urls.get(j).attr("abs:href");

                        Connection innerConn = Jsoup.connect(innerUrl);
                        Document innerDoc = innerConn.get();

                        Element ratings = innerDoc.selectFirst("div.dashboard-star__num");
                        double rating = Objects.isNull(ratings) ? 0.0 : Double.parseDouble(ratings.text());
                        rating = Math.round(rating * 10) / 10.0;

//                    sb.append(image).append("<br />");
//                    sb.append(title).append("<br />");
//                    sb.append(price).append("<br />");
//                    if (realPrice != salePrice) {
//                        sb.append(salePrice).append("<br />");
//                    }
//
//                    sb.append(instructor).append("<br />");
//                    sb.append(innerUrl).append("<br />");
//                    sb.append(description).append("<br />");
//                    sb.append(skill).append("<br />");
//                    sb.append(rating).append("<br />").append("<br />");


//                        Course course = new Course();
//                        course.setThumbnail(image);
//                        course.setTitle(title);
//                        course.setDescription(description);
//                        course.setRealprice(realPrice);
//                        course.setSaleprice(salePrice);
//                        course.setInstructor(instructor);
//                        course.setLink(innerUrl);
//                        course.setSkill(skill);
//                        course.setRating(rating);
                        courseRepository.save(new Course(null, image, title, description, realPrice, salePrice,
                                instructor, innerUrl, skill, rating));
                    } catch (JpaSystemException je) {
                        je.printStackTrace();
                    }

                }
            }
            return "다운로드 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "다운로드 실패";
        }
//        return sb.toString();

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
