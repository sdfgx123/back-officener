package fastcampus.team7.Livable_officener.controller;

import fastcampus.team7.Livable_officener.dto.*;
import fastcampus.team7.Livable_officener.global.util.APIDataResponse;
import fastcampus.team7.Livable_officener.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/building")
    public ResponseEntity<APIDataResponse<List<BuildingWithCompaniesDTO>>> getAllBuildings(@RequestParam("name") String keyword) {

        List<BuildingWithCompaniesDTO> result = signUpService.getBuildingWithCompanies(keyword);

        return APIDataResponse.of(HttpStatus.OK, result);

    }

    @PostMapping("/auth")
    public ResponseEntity<APIDataResponse<String>> getPhoneAuthCode(@RequestBody PhoneAuthRequestDTO request) {

        String phoneAuthCode = signUpService.getPhoneAuthCode(request);

        return APIDataResponse.of(HttpStatus.OK, phoneAuthCode);

    }

    @PostMapping("/confirm")
    public ResponseEntity<APIDataResponse<String>> confirmPhoneAuthCode(@RequestBody PhoneAuthConfirmDTO request) {

        boolean isConfirm = signUpService.confirmVerifyCode(request);

        return APIDataResponse.of(
                isConfirm ? HttpStatus.OK : HttpStatus.BAD_REQUEST,
                isConfirm ? "인증이 완료되었습니다." : "잘못된 인증 코드입니다.");

    }


    @PostMapping("/signup")
    public ResponseEntity<APIDataResponse<String>> signUp(@RequestBody SignUpRequestDTO request) {

        signUpService.signUp(request);

        return APIDataResponse.of(HttpStatus.OK, "회원가입에 성공했습니다.");

    }

    @PostMapping("/login")
    public ResponseEntity<APIDataResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = signUpService.login(request);

        return APIDataResponse.of(HttpStatus.OK, response);

    }

}
