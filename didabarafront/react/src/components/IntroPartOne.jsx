import { Button, Grid } from "@mui/material";
import React from "react";
import styled from "styled-components";
import InfoHardCodingText from "../items/InfoHardCodingText";
import { useNavigate } from "react-router-dom";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { loginState, userState } from "../config/Atom";

/**컴포넌트들 스타일 정의 */
const StyledGrid = styled(Grid)`
  width: 100%;
  height: 100vh;
  background-color: white;
`;
const StyledInnerGridLeft = styled(Grid)`
  border: 1px dotted black;
  display: flex;
  align-items: center;
  padding: 20px;
`;
const StyledInnerGridRight = styled(Grid)`
  border: 1px dotted black;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
`;
const StyledH2 = styled.h2`
  font: 52px;
`;
const StyledButton = styled(Button)`
  && {
    color: white;
    font-weight: bold;
    height: 55px;
    background-color: orange;
    transition: color 0.3s ease-in-out;
    transition: background-color 0.3s ease-in-out;
    &:hover {
      color: black;
      background-color: white;
    }
  }
`;

function IntroPartOne() {
  //유저 로그인상태 관리. 유저가 로그인이면 시작해보기 버튼은 대시보드 기능을하고
  //유저가 비 로그인 상태라면 시작해보기 버튼은 로그인창을 불러온다.
  const user = useRecoilValue(userState);
  const setLoginState = useSetRecoilState(loginState);
  const navi = useNavigate();

  const handleStart = () => {
    if (user.id !== null) {
      navi("/dashboard");
    } else {
      setLoginState(true);
    }
  };

  return (
    <StyledGrid container justifyContent="center" gap={0}>
      <StyledInnerGridLeft item lg={5} md={11} sm={11} xs={11}>
        {/**해당 img here 자리에 디다바라 일러스트? 추가 하고 싶어요*/}
        <StyledH2>img here</StyledH2>
      </StyledInnerGridLeft>
      <StyledInnerGridRight container item lg={5} md={11} sm={11} xs={11}>
        <InfoHardCodingText />
        버튼클릭시 로그인여부에따라 로그인창을 띄우거나
        <StyledButton variant="contained" onClick={handleStart}>
          지금 바로 시작해보기
        </StyledButton>
      </StyledInnerGridRight>
    </StyledGrid>
  );
}

export default IntroPartOne;
