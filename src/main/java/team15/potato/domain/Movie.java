package team15.potato.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 영화 (영화코드, 영화명(국문), 개봉일, 대표장르, 영화장르, 영화타입(장/단편), 감독, 영화 정보, 포스터이미지)
 */
@Data
@Entity
public class Movie {
    @Id /*영화 코드*/
    private String movieCd;

    /*영화명(국문)*/
    private String movieNm;

    /*개봉일*/
    private String openDt;

    /*대표 장르*/
    private String repGenreNm;

    /*영화 장르*/
    private String genreAlt;

    /*영화 타입(장편/단편)*/
    private String typeNm;

    /*영화 감독*/
    private String directors;

    /*영화 정보*/
    // private String movieInfo;

/*
    @Type(type = "blob")
    //포스터 이미지
    private byte image;
*/
}
