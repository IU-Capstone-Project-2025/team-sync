import { useEffect, useState } from 'react'
import SplashHeader from '../components/splashHeader'
import Footer from '../components/footer';
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../authConfig';

const backendHost = import.meta.env.VITE_BACKEND_HOST

async function handleTokenExchange(entraToken) {
  try {

    const backendResponse = await fetch(`${backendHost}/auth/api/v1/entra/login`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${entraToken}`,
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
    const initializeAndHandleRedirect = async () => {
      try {
        console.info("Initializing MSAL and handling redirect promise");
        await msalInstance.initialize();
        
        msalInstance.handleRedirectPromise().then(res => {
          console.info("Handled");

          if (res) {
            msalInstance.setActiveAccount(res.account);
            console.info("Redirect login successful", res.accessToken);
            handleTokenExchange(res.accessToken).then(result => {
              if (result && result.registered) {
                navigate('/home');
              } else if (result && !result.registered) {
                console.info("User needs to register");
                localStorage.setItem("entraToken", res.accessToken);
                setNeedsRegistration(true);
              }
            });
          }
        });

      } catch (err) {
        console.error("MSAL initialization or redirect handling failed:", err);
      }
    };

    initializeAndHandleRedirect();
  }, [navigate, msalInstance])

  // Handler for SSO button
  const handleSSOClick = () => {
    if (needsRegistration) {
      navigate('/register');
    } else {
      // fallback: trigger SSO login (should rarely be needed)
      msalInstance.loginRedirect(loginRequest);
    }
  };

  return (
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
        <img className='hidden lg:block pr-10 w-[30%] h-auto' src='./splashGraphic.jpg'
          alt="Two people looking at each other from open windows drawn in a minimalist style" />
      </div>
      <Footer />
    </div>
  );
}