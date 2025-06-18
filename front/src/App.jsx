import { useState } from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css'
import SplashScreen from './pages/splashScreen'
import SplashHeader from './components/splashHeader'
import HomeScreen from './pages/homeScreen'
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path = "/" element = {<SplashScreen />} />
        <Route path = "/home" element = {<HomeScreen />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
