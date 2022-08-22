import React from "react";
import styled from "styled-components";
import { Paper, Avatar } from "@mui/material";
import { Draggable } from "react-beautiful-dnd";

const StyledPaper = styled(Paper)`
  && {
    width: 300;
    border-radius: 0;
    cursor: grab;
    :hover {
      background-color: rgba(238, 238, 238, 1);
    }
  }
`;
const Wrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 10px;
`;
const ImgBlock = styled.div`
  display: flex;
  padding: 5px;
  margin: 5px;
  flex-direction: column;
  justify-content: space-around;
`;
const Img = styled(Avatar)`
  && {
    width: 50px;
    height: 50px;
  }
`;
const InfoLine = styled.div`
  display: flex;
  flex-direction: column;
  width: 200px;
  text-align: left;
  justify-content: flex-start;
  height: 80px;
`;
const H4 = styled.h4`
  color: rgba(53, 59, 72, 1);
  margin-block-start: 0rem;
  margin-block-end: 0.2rem;
`;
const H5 = styled.h5`
  color: ${(props) => props.theme.normal_Text_Color};
  margin-block-start: 0rem;
  margin-block-end: 0.3rem;
`;

function Profile({ img, username, title, text, idx, id }) {
  return (
    <Draggable key={id} draggableId={title} index={idx}>
      {(magic) => (
        <StyledPaper
          ref={magic.innerRef}
          {...magic.dragHandleProps}
          {...magic.draggableProps}
        >
          <Wrapper>
            <ImgBlock>
              <Img alt="userImage" src={img} />
              <H4>{username}</H4>
            </ImgBlock>
            <InfoLine>
              <H4 style={{ marginLeft: "5px" }}>{title}</H4>
              <H5 style={{ marginLeft: "5px" }}>{text}</H5>
            </InfoLine>
          </Wrapper>
        </StyledPaper>
      )}
    </Draggable>
  );
}

export default Profile;
