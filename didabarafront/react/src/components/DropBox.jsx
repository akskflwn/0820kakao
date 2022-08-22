import React from "react";
import { Droppable } from "react-beautiful-dnd";
import styled from "styled-components";
import Profile from "./Profile";

const Area = styled.div``;

const list = [
  {
    img: "a",
    username: "상돈",
    title: "프론트개발",
    text: "가나다 합시다.",
    id: 1,
  },
  {
    img: "b",
    username: "돈상",
    title: "개발프론트",
    text: "라마바 합시다.",
    id: 2,
  },
  {
    img: "c",
    username: "이상",
    title: "프개발론트",
    text: "사아자 합시다.",
    id: 3,
  },
  {
    img: "d",
    username: "상이",
    title: "프발론트개",
    text: "차카타 합시다.",
    id: 4,
  },
  {
    img: "e",
    username: "돈이",
    title: "프론개발트",
    text: "파하 합시다.",
    id: 5,
  },
  {
    img: "f",
    username: "이돈",
    title: "바바바바바",
    text: "오오오오 합시다.",
    id: 6,
  },
];

function DropBox() {
  return (
    <Droppable droppableId="profile">
      {(magic) => (
        <Area {...magic.droppableProps} ref={magic.innerRef}>
          {list.map((profile, idx) => (
            <Profile
              key={profile.id}
              img={profile.img}
              username={profile.username}
              title={profile.title}
              text={profile.text}
              idx={idx}
            />
          ))}
          {magic.placeholder}
        </Area>
      )}
    </Droppable>
  );
}

export default DropBox;
