import "./App.css";
import Router from "./Router";
import { RecoilRoot } from "recoil";
import { ThemeProvider } from "styled-components";
import { Theme } from "./Theme";

function App() {
  return (
    <div className="App">
      <RecoilRoot>
        <ThemeProvider theme={Theme}>
          <Router />
        </ThemeProvider>
      </RecoilRoot>
    </div>
  );
}

export default App;
