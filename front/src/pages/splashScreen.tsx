import { useEffect, useState, useRef } from 'react'
import SplashHeader from '../components/splashHeader'
import Footer from '../components/footer';
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../authConfig';
import SlidingBanner from '../components/imageBanner';
import FAQ from '../components/faq';
import ContactSection from '../components/contactSection';


const backendHost = import.meta.env.VITE_BACKEND_HOST

async function login(msalInstance) {
  const registrationData = {
    study_group: "string",
    description: "string",
    github_alias: crypto.randomUUID().toString().substring(0, 15),
    tg_alias: crypto.randomUUID().toString().substring(0, 15)
  };

  const account = msalInstance.getAllAccounts()[0];
  const tokenResponse = await msalInstance.acquireTokenSilent({
    ...loginRequest,
    account,
  });
  const accessToken = tokenResponse.accessToken;

  try {
    const res = await fetch(`${backendHost}/auth/api/v1/entra/login`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${accessToken}`,
        "Content-Type": "application/json"
      }
    });
    console.log("trying to login");
    const loginResult = await res.json();
    if (loginResult.success) {
      localStorage.setItem("backendToken", loginResult.data.access_token);
      console.log(loginResult.data.access_token);
    } else if (res.status === 409) {
      console.log("trying to register");
      const regRes = await fetch(`${backendHost}/auth/api/v1/entra/registration/student`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(registrationData)
      });
      const regData = await regRes.json();
      if (regData.success && regData.data && regData.data.access_token) {
        localStorage.setItem("backendToken", regData.data.access_token);
        console.log(regData.data.access_token);
      } else {
        console.error("Registration failed", regData.error || regData);
      }
    } else {
      console.error("Login failed", loginResult.error || loginResult);
    }
  } catch (error) {
    console.error("Login/registration failed", error);
  }
}

export default function SplashScreen() {
  const isAuthenticated = useIsAuthenticated();
  const navigate = useNavigate();
  const { instance: msalInstance } = useMsal();

  useEffect(() => {
    if (isAuthenticated) {
      (async () => {
        await login(msalInstance);
        if (localStorage.getItem("backendToken") !== null){
          navigate('/home');
        }
      })();
    }
  }, [isAuthenticated, navigate, msalInstance]);
  
  return(
    <div className='flex flex-col justify-between h-screen'>
      <SplashHeader/>
      <div className='flex flex-col justify-center items-center'>
        <div className='lg:flex lg:flex-row lg:justify-between lg:items-center pt-25 mr-5 ml-5 lg:mr-18 lg:ml-18'>
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
        <div className = 'pt-40'>
          <SlidingBanner/>
        </div>
        <div className = 'pt-10'></div>
        <div id="how-it-works" className='text-(--secondary-color) flex flex-col items-center py-20'>
          <h1 className='font-[Manrope] font-extrabold text-7xl'>How it works</h1>
          <div className='text-(--secondary-color) flex gap-15 justify-center pt-30 max-w-8xl mx-auto px-4'>
            <div className='flex flex-col items-center w-[350px]'>
              <div className='relative flex justify-center items-center pb-7'>
                <div className='w-16 h-16 rounded-full border-8 border-[#FFBB00] bg-transparent flex items-center justify-center'>
                  <p className='font-[Manrope] font-bold text-2xl text-gray-800'>1</p>
                </div>
              </div>
              <h3 className='font-[Inter] text-4xl font-semibold pb-6 text-center'>Set up your profile</h3>
              <p className='font-[Inter] text-2xl text-center'>Add your skills, preferred roles, and basic info — it only takes a minute.</p>
            </div>
            <div className='flex flex-col items-center w-[350px]'>
              <div className='relative flex justify-center items-center pb-7'>
                <div className='w-16 h-16 rounded-full border-8 border-[#FFBB00] bg-transparent flex items-center justify-center'>
                  <p className='font-[Manrope] font-bold text-2xl text-gray-800'>2</p>
                </div>
              </div>
              <h3 className='font-[Inter] text-4xl font-semibold pb-6 text-center'>Explore projects</h3>
              <p className='font-[Inter] text-2xl text-center'>Let AI-recommendations suggest the best-fit options for you.</p>
            </div>
            <div className='flex flex-col items-center w-[350px]'>
              <div className='relative flex justify-center items-center pb-7'>
                <div className='w-16 h-16 rounded-full border-8 border-[#FFBB00] bg-transparent flex items-center justify-center'>
                  <p className='font-[Manrope] font-bold text-2xl text-gray-800'>3</p>
                </div>
              </div>
              <h3 className='font-[Inter] text-4xl font-semibold pb-6 text-center'>Send a request</h3>
              <p className='font-[Inter] text-2xl text-center'>Apply to project or add it to your favorites to decide later.</p>
            </div>
          </div>
        </div>
        <div className = 'pt-20'></div>
        <hr className='text-(--primary-color) w-[75%] border-2 rounded-2xl'/>
        
        {/* For students section */}
        <div className='text-(--secondary-color) flex flex-col items-center py-32'>
          <div id="for-students" className='scroll-mt-10'></div>
          <h1 className='font-[Manrope] font-extrabold text-7xl pb-32'>For students</h1>

          <div className='flex gap-24 justify-center max-w-full mx-auto px-8 pb-24'>
            <div className='flex items-center gap-50 w-[1100px]'>
              <div className='flex flex-col flex-1'>
                <h3 className='font-[Inter] text-5xl font-semibold pb-8'>Find a team for educational purposes</h3>
                <p className='font-[Inter] text-2xl leading-relaxed'>
                  TeamSync connects students from around the world, helping you build strong teams for academic and 
                  innovation projects. Expand your network, collaborate globally, and unlock new growth opportunities
                </p>
              </div>
              <img className='w-96 h-auto flex-shrink-0' src='./book.svg' alt="Book illustration" />
            </div>
          </div>
          
          <div className='flex gap-24 justify-center max-w-full mx-auto px-8'>
            <div className='flex items-center gap-50 w-[1100px]'>
              <img className='w-96 h-auto flex-shrink-0' src='./rocket.jpg' alt="Rocket illustration" />
              <div className='flex flex-col flex-1'>
                <h3 className='font-[Inter] text-5xl font-semibold pb-8'>Build your network while studying</h3>
                <p className='font-[Inter] text-2xl leading-relaxed'>
                  Connect with students from different universities and majors. Collaborate on projects while building professional 
                  relationships for your future career. Join a global community of learners and turn academic partnerships into valuable 
                  connections that will benefit you.
                </p>
              </div>
            </div>
          </div>
        </div>
        
        <hr className='text-(--primary-color) w-[75%] border-2 rounded-2xl'/>
        
        {/* For teachers section */}
        <div className='text-(--secondary-color) flex flex-col items-center py-32'>
          <div id="for-teachers" className='scroll-mt-10'></div>
          <h1 className='font-[Manrope] font-extrabold text-7xl pb-32'>For teachers</h1>

          <div className='flex gap-24 justify-center max-w-full mx-auto px-8 pb-24'>
            <div className='flex items-center gap-50 w-[1100px]'>
              <div className='flex flex-col flex-1'>
                <h3 className='font-[Inter] text-5xl font-semibold pb-8'>Help your students form balanced teams</h3>
                <p className='font-[Inter] text-2xl leading-relaxed'>
                  TeamSync takes the hassle out of team formation by automatically analyzing student skills, interests, and availability. 
                  Create balanced groups for assignments, research projects, and collaborative work while ensuring each team has 
                  complementary expertise.
                </p>
              </div>
              <img className='w-96 h-auto flex-shrink-0' src='./balance.svg' alt="Balance illustration" />
            </div>
          </div>
          
          <div className='flex gap-24 justify-center max-w-full mx-auto px-8'>
            <div className='flex items-center gap-50 w-[1100px]'>
              <img className='w-96 h-auto flex-shrink-0 rounded-lg' src='./clash.png' alt="Clash Royale illustration" />
              <div className='flex flex-col flex-1'>
                <h3 className='font-[Inter] text-5xl font-semibold pb-8'>Streamline project management</h3>
                <p className='font-[Inter] text-2xl leading-relaxed'>
                  Reduce administrative overhead while improving student outcomes. Monitor team progress, facilitate better collaboration, 
                  and focus on what matters most - teaching. Let AI handle the complex task of optimal team composition 
                  based on skills, experience, and learning objectives.
                </p>
              </div>
            </div>
          </div>
        </div>
        <hr className='text-(--primary-color) w-[75%] border-2 rounded-2xl'/>
        
        <FAQ />
        
        <hr className='text-(--primary-color) w-[75%] border-2 rounded-2xl'/>
        
        <ContactSection />
        
      </div>
      <Footer />
    </div>
  );
}