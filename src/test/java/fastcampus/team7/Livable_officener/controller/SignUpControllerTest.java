package fastcampus.team7.Livable_officener.controller;

import fastcampus.team7.Livable_officener.dto.BuildingWithCompaniesDTO;
import fastcampus.team7.Livable_officener.dto.CompanyDTO;
import fastcampus.team7.Livable_officener.service.SignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignUpController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SignUpControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SignUpService signUpService;

    @Test
    @WithMockUser
    @DisplayName("[GET] /api/building?name={buildingName} - Controller 성공 테스트")
    void searchBuildingWithCompanies_Success() throws Exception {

        // given
        final String keyword = "미";
        List<BuildingWithCompaniesDTO> buildingWithCompaniesDTOList = new ArrayList<>();
        BuildingWithCompaniesDTO buildingWithCompaniesDTO = BuildingWithCompaniesDTO
                .builder()
                .id(1L)
                .buildingName("미왕빌딩")
                .buildingAddress("서울 강남구 강남대로 364")
                .build();

        List<CompanyDTO> companyDTOS = new ArrayList<>();
        companyDTOS.add(
                CompanyDTO.builder()
                        .name("진회사")
                        .companyNum("A동 101호")
                        .build());
        companyDTOS.add(
                CompanyDTO.builder()
                        .name("칠리버블")
                        .companyNum("A동 102호")
                        .build());
        companyDTOS.add(
                CompanyDTO.builder()
                        .name("식스센스")
                        .companyNum("A동 103호")
                        .build());

        buildingWithCompaniesDTO.setCompanies(companyDTOS);
        buildingWithCompaniesDTOList.add(buildingWithCompaniesDTO);

        // when
        when(signUpService.getBuildingWithCompanies(keyword)).thenReturn(buildingWithCompaniesDTOList);

        // then
        mvc.perform(get("/api/building").param("name", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].buildingName", is("미왕빌딩")))
                .andExpect(jsonPath("$.data[0].buildingAddress", is("서울 강남구 강남대로 364")))
                .andExpect(jsonPath("$.data[0].companies", hasSize(3)))
                .andExpect(jsonPath("$.data[0].companies[0].name", is("진회사")))
                .andExpect(jsonPath("$.data[0].companies[1].name", is("칠리버블")))
                .andExpect(jsonPath("$.data[0].companies[2].name", is("식스센스")))
                .andReturn();

    }


    @Test
    @WithMockUser
    @DisplayName("[GET] /api/building?name={buildingName} - Controller 실패 테스트")
    void searchBuildingWithCompanies_Failure() throws Exception {

        // given
        final String keyword = "없는 빌딩";

        // when
        when(signUpService.getBuildingWithCompanies(keyword)).thenReturn(Collections.emptyList());

        // then
        mvc.perform(get("/api/building").param("name", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}