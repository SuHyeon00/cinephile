package team15.potato.api;

import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import team15.potato.domain.Movie;
import team15.potato.domain.MovieRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * 영화 상세정보 받아오기
 */

@Component
@RequiredArgsConstructor
public class MovieListApi {

    private final MovieRepository movieRepository;

    // 발급키
    String key = "60dd3402d40b9f20acf20fe34fb41a1e";

    public void movieList() {
        // api 요청 결과 저정할 변수
        String movieListResponse = "";

        // 한 페이지당 요청할 row 수
        String itemPerPage = "100";

        try {
            // Kobis Open API 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // Movie List 호출 (boolean isJson, Map paramMap)
            // itemPerPage = 100, curPage = 1 ~ 50
            for (int curPage = 50; curPage <= 100; curPage++) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("curPage", Integer.toString(curPage));
                paramMap.put("itemPerPage", itemPerPage);

                movieListResponse = service.getMovieList(true, paramMap);

                // JSON parser
                JSONParser jsonParser = new JSONParser();
                Object obj = jsonParser.parse(movieListResponse);
                JSONObject jsonObject = (JSONObject) obj;

                // Parsing
                JSONObject parse_movieListResult = (JSONObject) jsonObject.get("movieListResult");

                /* movieList:[{movieCd, movieNm, openDt, repGenreNm, genreAlt, typeNm, directors}]*/
                JSONArray parse_movieList = (JSONArray) parse_movieListResult.get("movieList");
                for (int i = 0; i < parse_movieList.size(); i++) {
                    JSONObject movie = (JSONObject) parse_movieList.get(i);

                    String directors = "";
                    JSONArray parse_directors = (JSONArray) movie.get("directors");
                    JSONObject director = (JSONObject) parse_directors.get(0);
                    directors = (String) director.get("peopleNm");
                    for (int j = 1; j < parse_directors.size(); j++) {
                        director = (JSONObject) parse_directors.get(i);
                        directors += "|" + (String) director.get("peopleNm");
                    }

                    Movie movieSave = new Movie();
                    movieSave.setMovieCd((String) movie.get("movieCd"));
                    movieSave.setMovieNm((String) movie.get("movieNm"));
                    movieSave.setOpenDt((String) movie.get("openDt"));
                    movieSave.setRepGenreNm((String) movie.get("repGenreNm"));
                    movieSave.setGenreAlt((String) movie.get("genreAlt"));
                    movieSave.setTypeNm((String) movie.get("typeNm"));
                    movieSave.setDirectors(directors);

                    movieRepository.save(movieSave);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
