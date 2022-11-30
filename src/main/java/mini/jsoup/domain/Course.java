package mini.jsoup.domain;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String thumbnail;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private Integer realprice;
    @Column
    private Integer saleprice;
    @Column
    private String instructor;
    @Column
    private String link;
    @Column
    private String skill;
    @Column
    private double rating;
}
