import React from "react";
import { Button, Grid, Input, InputLabel, Typography } from "@mui/material";
import styled from "styled-components";
import { FormControl } from "@mui/material";
import { KakaoLoginAPI } from "../config/KakaoApi";
import axios from "axios";
import { useSetRecoilState } from "recoil";
import { userState } from "../config/Atom";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";

/**백엔드 로그인 어노테이션 주소 */
const LOGIN_REQUEST_ADDRESS = "http://192.168.0.187:8080/auth/signin";

/**컴포넌트 스타일 정의 */
const StyledInput = styled(FormControl)`
  && {
    width: 90%;
  }
`;
const StyledContainer = styled(Grid)`
  && {
    height: 80%;
    min-height: 685px;
    display: flex;
    justify-content: center;
    flex-direction: column;
  }
`;
const StyledGrid = styled(Grid)`
  && {
    width: 60%;
    margin-bottom: 20px;
    padding-top: 20px;
  }
`;
const StyledButton = styled(Button)`
  && {
    width: 100%;
    color: black;
    border: black solid 1px;
  }
`;
const StyledForm = styled.form`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;
function LoginInput() {
  const { register, watch, handleSubmit } = useForm();
  /**유저 상태관리를 위한 Recoil 호출.
   * 괄호안에들어가는 userState 는 config 폴더의 Atom 에 정의해 두었습니다.*/
  const setUser = useSetRecoilState(userState);

  /**페이지 리디렉션용 useNavigate */
  const navi = useNavigate();

  /**온클릭 이벤트
   * 카카오 로그인용 새창을 띄운다.
   */
  const openKakaoLogin = () => {
    window.open(KakaoLoginAPI);
  };

  /**이벤트 발생 폼으로부터 데이터를 받아
   * 각각 username 과 password 로 할당하여
   * 백엔드 로그인 주소로 보내준다.
   *
   * respone 값이 200(ok)이고 응답데이터에 유저의 아이디가 비어있지 않으면
   * (response 가 200인데도 유저의 데이터가 null 인경우도 있을수 있으므로)
   *
   * Recoil 의 setUser 함수로 유저의 상태를 로그인 상태로 만든다.
   */
  const sendLoginRequest = (data) => {
    console.log(data);

    axios
      .post(LOGIN_REQUEST_ADDRESS, data)
      .then((res) => {
        if (res.status === 200 && res.data.id) {
          setUser(res.data);
          console.log("data response printing....:", res);
          navi("/dashboard");
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <StyledContainer container>
      <StyledForm onSubmit={handleSubmit(sendLoginRequest)}>
        <StyledGrid item>
          <StyledInput variant="standard">
            <InputLabel htmlFor="username">ID</InputLabel>
            <Input {...register("username" , {required:""})} id="username" name="username" />
          </StyledInput>
        </StyledGrid>
        <StyledGrid item>
          <StyledInput variant="standard">
            <InputLabel htmlFor="password">Password</InputLabel>
            <Input {...register("password")} id="password" name="password" />
          </StyledInput>
        </StyledGrid>
        <StyledGrid container item justifyContent="center" gap={3}>
          <Grid item xs={5}>
            <StyledButton type="submit">로그인</StyledButton>
          </Grid>
          <Grid item xs={5}>
            <StyledButton
              onClick={() => {
                navi("/join");
              }}
            >
              회원가입
            </StyledButton>
          </Grid>
        </StyledGrid>
        <StyledGrid>
          <Grid item xs={12}>
            <StyledButton style={{ width: "90%" }} onClick={openKakaoLogin}>
              kakao login
            </StyledButton>
          </Grid>
        </StyledGrid>
      </StyledForm>
      <Typography variant="body2" color="textSecondary" align="center">
        Copyright ©{" "}
        <i className="fa-brands fa-github" style={{ fontSize: "2rem" }}></i>{" "}
        {new Date().getFullYear()}
        BitCamp 221기 2022
      </Typography>
    </StyledContainer>
  );
}

export default LoginInput;
