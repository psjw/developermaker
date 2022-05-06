package com.psjw.dmaker.service;

import com.psjw.dmaker.entity.Developer;
import com.psjw.dmaker.repository.DeveloperRepository;
import com.psjw.dmaker.type.DeveloperLevel;
import com.psjw.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    //ACID
    //Atomic
    //Consistency
    //Isolation
    //Durability
    @Transactional
    public void createDeveloper(){
        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(2)
                .name("Olaf")
                .age(5)
                .build();
        developerRepository.save(developer);
    }
}
