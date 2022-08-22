import React from "react";
import { Grid } from "@mui/material";
import styled from "styled-components";
import DnDropContext from "../components/DnDropContext";

const Item = styled.div`
  /* border: 1px solid black; */
`;

function DashBoard() {
  return (
    <div>
      <Grid container style={{ border: "1px solid black" }}>
        <Grid item xs={2}>
          <Item>
            <Item style={{ border: "1px solid black" }}>Menu?</Item>
            <DnDropContext />
          </Item>
        </Grid>
        <Grid item xs={7} style={{ border: "1px solid black" }}>
          <Item
            style={{
              border: "1px solid black",
              position: "relative",
            }}
          >
            Main document section
          </Item>
          <Item>Document here</Item>
        </Grid>
        <Grid item xs={3}>
          <Item>Community tab</Item>
        </Grid>
      </Grid>
    </div>
  );
}

export default DashBoard;
