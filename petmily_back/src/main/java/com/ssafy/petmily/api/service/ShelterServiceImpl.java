package com.ssafy.petmily.api.service;

import com.ssafy.petmily.db.entity.Shelter;
import com.ssafy.petmily.db.repository.ShelterRepositorySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterServiceImpl implements ShelterService{

    @Autowired
    ShelterRepositorySupport shelterRepositorySupport;

    @Override
    public List<Shelter> searchShelterCode(String word) {
        return shelterRepositorySupport.searchShelterByName(word);
    }
}
