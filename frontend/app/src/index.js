import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { ThemeProvider, CssBaseline } from "@mui/material";
import TeddyAssistant from './components/assistant/TeddyAssistant';
import { TeddyProvider,useTeddy } from "./context/TeddyContext";


import App from "./App";
import theme from "./theme/theme";
import { AuthProvider } from "./context/AuthContext";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  // ✅ correct order
<BrowserRouter>
  <ThemeProvider theme={theme}>
    <CssBaseline />
    <AuthProvider>
      <TeddyProvider>
        <TeddyAssistant />
        <App />
      </TeddyProvider>
    </AuthProvider>
  </ThemeProvider>
</BrowserRouter>
);
