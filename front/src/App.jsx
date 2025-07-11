import { Routes, Route } from 'react-router-dom';
import './App.css'
import SplashScreen from './pages/splashScreen'
import HomeScreen from './pages/homeScreen'
import ProjectScreen from "./pages/projectScreen"
import CreateProjectScreen from './pages/createProjectScreen';
import ResponseScreen from './pages/responsesScreen';
import LikesScreen from './pages/likesScreen';
import ProfileScreen from './pages/profileScreen';

function App() {
  return (
    <Routes>
      <Route path = "/" element = {<SplashScreen />} />
      <Route path = "/home" element = {<HomeScreen />} />
      <Route path = "/projects" element = {<ProjectScreen />} />
      <Route path = "/projects/create" element = {<CreateProjectScreen/>} />
      <Route path = "/responses" element = {<ResponseScreen/>} />
      <Route path = "/likes" element = {<LikesScreen/>} />
      <Route path = "/profile" element = {<ProfileScreen/>} />
    </Routes>
  )
}

export default App
