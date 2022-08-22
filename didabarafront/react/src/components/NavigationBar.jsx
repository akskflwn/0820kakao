import React from "react";
import { Grid, Button, Typography } from "@mui/material";
import PictureAsPdfSharpIcon from "@mui/icons-material/PictureAsPdfSharp";
import LoginSharpIcon from "@mui/icons-material/LoginSharp";
import LogoutSharpIcon from "@mui/icons-material/LogoutSharp";
import { useNavigate } from "react-router-dom";
import { useRecoilState, useResetRecoilState, useSetRecoilState } from "recoil";
import { loginState, userState } from "../config/Atom";
import styled from "styled-components";

const StyledButton = styled(Button)`
  && {
    color: rgba(220, 221, 225, 1);
  }
`;

function NavigationBar() {
  const setLoginState = useSetRecoilState(loginState);
  const navi = useNavigate();

  /**로그아웃 버튼 이벤트시
   * Recoil 의 정보를 리셋시키는 함수.
   * user 의 defautl 값인 id:null 이 된다.
   */
  const userLogout = useResetRecoilState(userState);

  /** 이벤트에 따라 유저의 상태를 관리하기 위한 Recoil */
  const [user, setUser] = useRecoilState(userState);

  // console.log(user);
  return (
    <Grid
      container
      justifyContent="center"
      alignItems="center"
      style={{
        position: "sticky",
        top: 0,
        backgroundColor: "rgba(47, 54, 64,1.0)",
        padding: "10px",
        zIndex: "5",
      }}
    >
      <Grid
        container
        item
        xs={6}
        md={5}
        spacing={2}
        alignItems="center"
        justifyContent="start"
      >
        <Grid item display="flex" alignItems="center" ml={2}>
          <PictureAsPdfSharpIcon
            style={{
              cursor: "pointer",
              color: "rgba(220, 221, 225, 1)",
            }}
            fontSize="large"
            onClick={() => {
              navi("/");
            }}
          />
        </Grid>
      </Grid>

      <Grid container item xs={6} md={5} justifyContent="end">
        {user.id ? (
          <Grid item display="flex" alignItems="center" mr={3}>
            <StyledButton
              variant="black"
              onClick={() => {
                userLogout();
                navi("/");
              }}
            >
              <LogoutSharpIcon />
              <Typography ml={1}>logout</Typography>
            </StyledButton>
          </Grid>
        ) : (
          <Grid item display="flex" alignItems="center" mr={3}>
            <StyledButton
              variant="black"
              onClick={() => {
                setLoginState(true);
              }}
            >
              <LoginSharpIcon />
              <Typography ml={1}>login</Typography>
            </StyledButton>
          </Grid>
        )}
        {user.id ? (
          <Grid item mr={2}>
            <Button
              variant="black"
              onClick={() => {
                navi("/");
              }}
            >
              <Typography>maypage</Typography>
            </Button>
          </Grid>
        ) : (
          <Grid item mr={2}>
            <StyledButton
              variant="black"
              onClick={() => {
                navi("/join");
              }}
            >
              <Typography>Join</Typography>
            </StyledButton>
          </Grid>
        )}
      </Grid>
    </Grid>
  );
}

export default NavigationBar;
