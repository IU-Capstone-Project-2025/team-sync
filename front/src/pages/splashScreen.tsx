import { useEffect } from 'react'
import SplashHeader from '../components/splashHeader'
import Footer from '../components/footer';
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../authConfig';

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
    const res = await fetch("/auth/api/v1/entra/login", {
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
      const regRes = await fetch("/auth/api/v1/entra/registration/student", {
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
    if (isAuthenticated && localStorage.getItem("backendToken") !== null) {
      (async () => {
        await login(msalInstance);
        navigate('/home');
      })();
    }
  }, [isAuthenticated, navigate, msalInstance]);
  
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