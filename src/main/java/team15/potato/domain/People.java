package team15.potato.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 영화인 (배우 / 감독)
 */

@Data
@Entity
public class People {
    @Id /*영화인 코드*/
    private String peopleCd;

    /*영화인 명(국문)*/
    private String peopleNm;

    /*영화인 명(영문)*/
    private String peopleNmEn;

    /*분야 (배우/감독)*/
    private String repRoleNm;

    /*필모리스트*/
    private String filmoNames;

    /*성별*/
    private String sex;
}
