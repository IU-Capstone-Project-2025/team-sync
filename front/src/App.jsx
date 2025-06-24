import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, useNavigate} from 'react-router-dom';
import './App.css'
import SplashScreen from './pages/splashScreen'
import HomeScreen from './pages/homeScreen'
import ProjectScreen from "./pages/projectScreen"
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
import CreateProjectScreen from './pages/createProjectScreen';

function App() {
  return (
    <Routes>
      <Route path = "/" element = {<SplashScreen />} />
      <Route path = "/home" element = {<HomeScreen />} />
      <Route path = "/projects" element = {<ProjectScreen />} />
      <Route path = "/create_project" element = {<CreateProjectScreen/>} />
    </Routes>
  )
}

export default App
