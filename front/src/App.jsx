import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, useNavigate } from 'react-router-dom';
import './App.css'
import SplashScreen from './pages/splashScreen'
import HomeScreen from './pages/homeScreen'
import ProjectScreen from "./pages/projectScreen"
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
function App() {
  const isAuthenticated = useIsAuthenticated();
  const { inProgress } = useMsal();
  const navigate = useNavigate();

  useEffect(() => {
    if (inProgress !== "none") return;
    if (isAuthenticated) {
      navigate("/home");
    }
    else {
      navigate("/");
    }
  }, [isAuthenticated, navigate]);
  return (
    <Routes>
      <Route path = "/" element = {<SplashScreen />} />
      <Route path = "/home" element = {<HomeScreen />} />
      {/* <Route path = "/projects" element = {<ProjectScreen />} /> */}
    </Routes>
  )
}

export default App
