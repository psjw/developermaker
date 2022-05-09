package com.psjw.dmaker.service;

import com.psjw.dmaker.code.StatusCode;
import com.psjw.dmaker.dto.CreateDeveloper;
import com.psjw.dmaker.dto.DeveloperDetailDto;
import com.psjw.dmaker.dto.DeveloperDto;
import com.psjw.dmaker.dto.EditDeveloper;
import com.psjw.dmaker.entity.Developer;
import com.psjw.dmaker.entity.RetiredDeveloper;
import com.psjw.dmaker.exception.DMakerErrorCode;
import com.psjw.dmaker.exception.DMakerException;
import com.psjw.dmaker.repository.DeveloperRepository;
import com.psjw.dmaker.repository.RetiredDeveloperRepository;
import com.psjw.dmaker.type.DeveloperLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;
//    private final EntityManager entityManager;

    //ACID
    //Atomic
    //Consistency
    //Isolation
    //Durability
    @Transactional
    public CreateDeveloper.Response createDeveloper(@NonNull CreateDeveloper.Request request) {
//        EntityTransaction transaction = entityManager.getTransaction();
        validateCreateDeveloperRequest(request);
        //business logic start
        return CreateDeveloper.Response.fromEntity(
                developerRepository.save(createDeveloperFromRequest(request))
        );

//        transaction.commit();
//        transaction.rollback();
    }

    private Developer createDeveloperFromRequest(CreateDeveloper.Request request) {
        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .statusCode(StatusCode.EMPLOYED)
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .build();
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business validation
//        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                }));
    }

    private Developer getDeveloperByMemberId(String memberId) {
        return  developerRepository.findByMemberId(memberId)
                .orElseThrow(()-> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
    }

    @Transactional
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
       /* return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));*/
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request);
/*        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));*/
        return DeveloperDetailDto.fromEntity(setDeveloperFromRequest(request, getDeveloperByMemberId(memberId)));
    }

    private Developer setDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());
        return developer;
    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request) {
//        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());
    }

    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
//        if(experienceYears < developerLevel.getMinExperienceYears() || experienceYears > developerLevel.getMaxExperienceYears()){
//            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
//        }

        developerLevel.validateExperienceYears(experienceYears);

//        if (developerLevel == DeveloperLevel.SENIOR && experienceYears < 10) {
//            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
//        }
//        if (developerLevel == DeveloperLevel.JUNGNIOR
//                && (experienceYears < DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS || experienceYears > DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS)) {
//            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
//        }
//        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
//            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
//        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        //1. EMPLOYED -> RETIRED
//        Developer developer = developerRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
        Developer developer = getDeveloperByMemberId(memberId);
        developer.setStatusCode(StatusCode.RETIRED);
//        developerRepository.save(developer);
//        if(developer!=null)
//            throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);
        //2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);
    }
}
