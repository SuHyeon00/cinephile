package team15.potato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team15.potato.api.PeopleListApi;

@Service
@RequiredArgsConstructor
public class PeopleListApiService {
    private final PeopleListApi peopleListApi;

    public void insertPeople() {
        peopleListApi.peopleList();
    }
}
