package team15.potato.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import team15.potato.domain.People;
import team15.potato.domain.PeopleRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * 영화인 목록 받아오는 API
 */

@Component
@RequiredArgsConstructor
public class PeopleListApi {

    private final PeopleRepository peopleRepository;

    // 발급키
    String key = "60dd3402d40b9f20acf20fe34fb41a1e";

    public void peopleList() {
        // api 요청 결과 저정할 변수
        String peopleListResponse = "";
        String peopleResponse = "";

        // 한 페이지당 요청할 row 수
        String itemPerPage = "100";

        // 배우&감독만 저장하기 위해 역할명 저장할 변수
        String repRoleNm = "";

        try {
            // Kobis Open API 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 영화인 목록 호출 (boolean isJson, Map paramMap)
            // itemPerPage = 100, curPage = 50 ~ 100
            for (int curPage = 50; curPage <= 100; curPage++) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("curPage", Integer.toString(curPage));
                paramMap.put("itemPerPage", itemPerPage);

                peopleListResponse = service.getPeopleList(true, paramMap);

                // JSON Parser
                JSONParser jsonParser = new JSONParser();
                Object obj = jsonParser.parse(peopleListResponse);
                JSONObject jsonObject = (JSONObject) obj;

                // Parsing
                /* peopleList:[{peopleCd, peopleNm, peopleNmEn, repRoleNm, filmoNames}] */
                JSONObject parse_peopleListResult = (JSONObject) jsonObject.get("peopleListResult");

                JSONArray parse_peopleList = (JSONArray) parse_peopleListResult.get("peopleList");
                ObjectMapper objectMapper = new ObjectMapper();
                for (int i = 0; i < parse_peopleList.size(); i++) {
                    JSONObject people = (JSONObject) parse_peopleList.get(i);

                    // DB 저장 -> repRoleNm이 배우 혹은 감독인 경우만 저장
                    repRoleNm = (String) people.get("repRoleNm");
                    if (repRoleNm.equals("감독") || repRoleNm.equals("배우")) {
                        // JSON Object -> Java Object(Entity) 변환
                        People peopleSave = objectMapper.readValue(people.toString(), People.class);

                        // 영화인 상세 정보 API에서 성별 조회
                        peopleResponse = service.getPeopleInfo(true, peopleSave.getPeopleCd());

                        obj = jsonParser.parse(peopleResponse);
                        jsonObject = (JSONObject) obj;
                        JSONObject parse_peopleInfoResult = (JSONObject) jsonObject.get("peopleInfoResult");
                        JSONObject parse_peopleInfo = (JSONObject) parse_peopleInfoResult.get("peopleInfo");
                        peopleSave.setSex((String) parse_peopleInfo.get("sex"));

                        peopleRepository.save(peopleSave);
                    }
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
