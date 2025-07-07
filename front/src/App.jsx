import { Routes, Route } from 'react-router-dom';
import './App.css'
import SplashScreen from './pages/splashScreen'
import HomeScreen from './pages/homeScreen'
import ProjectScreen from "./pages/projectScreen"
import CreateProjectScreen from './pages/createProjectScreen';
import ProfileScreen from './pages/profileScreen';

function App() {
  return (
    <Routes>
      <Route path = "/" element = {<SplashScreen />} />
      <Route path = "/home" element = {<HomeScreen />} />
      <Route path = "/projects" element = {<ProjectScreen />} />
      <Route path = "/create_project" element = {<CreateProjectScreen/>} />
      <Route path = "/profile" element = {<ProfileScreen/>} />
    </Routes>
  )
}

export default App
