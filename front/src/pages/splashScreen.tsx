import { useEffect, useState } from 'react'
import SplashHeader from '../components/splashHeader'
import Footer from '../components/footer';
import { useIsAuthenticated } from '@azure/msal-react';
import { useNavigate } from 'react-router-dom';
export default function SplashScreen() {
  const isAuthenticated = useIsAuthenticated();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/home');
    }
  }, [isAuthenticated, navigate]);
  
  return(
    <div className='flex flex-col justify-between h-screen'>
      <SplashHeader/>
      <div className='lg:flex lg:flex-row lg:justify-between lg:items-center mr-5 ml-5 lg:mr-18 lg:ml-18'>
        <div className='flex flex-col justify-center items-center gap-5 lg:w-[60%]'>
          <h1 className='text-(--secondary-color) font-[Manrope] font-extrabold lg:text-7xl text-3xl'>
            Build your dream team for every project.
          </h1>
          <h2 className='text-(--accent-color-1) font-[Manrope] text-md lg:text-2xl'>
            TeamSync uses structured data and AI recommendations to help students form balanced, 
            high-performing teams — reducing overhead for faculty and improving outcomes.
          </h2>
        </div>
        <img className = 'hidden lg:block pr-10 w-[30%] h-auto' src='./splashGraphic.jpg' 
        alt="Two people looking at each other from open windows drawn in a minimalist style" />
      </div>
      <Footer />
    </div>
  );
}