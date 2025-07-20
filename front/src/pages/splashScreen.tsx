import { useEffect, useState } from 'react'
import SplashHeader from '../components/splashHeader'
import Footer from '../components/footer';
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../authConfig';

const backendHost = import.meta.env.VITE_BACKEND_HOST

async function login(msalInstance) {
  try {
    const accounts = msalInstance.getAllAccounts();
    
    if (accounts.length === 0) {
      const loginResponse = await msalInstance.loginPopup(loginRequest);
      if (!loginResponse.account) throw new Error("Login failed");
      return await handleTokenExchange(msalInstance, loginResponse.account);
    }

    const tokenResponse = await msalInstance.acquireTokenSilent({
      ...loginRequest,
      account: accounts[0]
    }).catch(async (error) => {
      console.log("Silent token acquisition failed, trying popup", error);
      return msalInstance.loginPopup(loginRequest);
    });

    return await handleTokenExchange(msalInstance, tokenResponse.account);

  } catch (error) {
    console.error("Login failed:", error);
    throw error;
  }
}

async function handleTokenExchange(msalInstance, account) {
  try {
    const tokenResponse = await msalInstance.acquireTokenSilent({
      ...loginRequest,
      account
    });

    const backendResponse = await fetch(`${backendHost}/auth/api/v1/entra/login`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${tokenResponse.accessToken}`,
        "Content-Type": "application/json"
      }
    });

    const loginResult = await backendResponse.json();

    if (!loginResult.success) {
      if (backendResponse.status === 409) {
        return { registered: false };
      }
      throw new Error("Backend login failed");
    }

    localStorage.setItem("entraToken", tokenResponse.accessToken);
    localStorage.setItem("backendToken", loginResult.data.access_token);
    
    return { registered: true };

  } catch (error) {
    console.error("Token exchange failed:", error);
    throw error;
  }
}

export default function SplashScreen() {
  const isAuthenticated = useIsAuthenticated();
  const navigate = useNavigate();
  const { instance: msalInstance } = useMsal();
  const [needsRegistration, setNeedsRegistration] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      (async () => {
        const result = await login(msalInstance);
        if (result && result.registered) {
          navigate('/home');
        } else if (result && !result.registered) {
          setNeedsRegistration(true);
        }
      })();
    }
  }, [isAuthenticated, navigate, msalInstance]);
  
  // Handler for SSO button
  const handleSSOClick = () => {
    if (needsRegistration) {
      navigate('/register');
    } else {
      // fallback: trigger SSO login (should rarely be needed)
      msalInstance.loginRedirect(loginRequest);
    }
  };

  return(
    <div className='flex flex-col justify-between h-screen'>
      <SplashHeader onSSOClick={handleSSOClick} />
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