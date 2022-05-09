package com.psjw.dmaker.service;


import com.psjw.dmaker.code.StatusCode;
import com.psjw.dmaker.dto.CreateDeveloper;
import com.psjw.dmaker.dto.DeveloperDetailDto;
import com.psjw.dmaker.entity.Developer;
import com.psjw.dmaker.exception.DMakerErrorCode;
import com.psjw.dmaker.exception.DMakerException;
import com.psjw.dmaker.repository.DeveloperRepository;
import com.psjw.dmaker.repository.RetiredDeveloperRepository;
import com.psjw.dmaker.type.DeveloperLevel;
import com.psjw.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks //가짜
    private DMakerService dMakerService;
    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private CreateDeveloper.Request getCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .name("name")
                .memberId("memberId")
                .age(32)
                .build();
    }

    @Test
    public void testSomething() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getExperienceYears());

    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);
        //Argumemt 캡처
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);
        //when
        CreateDeveloper.Response developer = dMakerService
                .createDeveloper(getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, 12));
        //then
        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        //Argumemt 캡처
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);
        //when
        DMakerException digestException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, 12)));

        //then
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, digestException.getDMakerErrorCode());
    }


    @Test
    void createDeveloperTest_fail_with_unmatched_senior() {
        //given
        //when
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService
                        .createDeveloper(getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, 8)));
        //then
        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

    }
}