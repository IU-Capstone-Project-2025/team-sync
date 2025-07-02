import React from 'react';
import { useNavigate } from 'react-router-dom';
import {useMsal} from "@azure/msal-react"
import {loginRequest} from "../authConfig"
export default function SignUpButton(){
  const navigate = useNavigate();
  const {instance, accounts}  = useMsal();
  const handleLogin = async () => {
    try {
      await instance.loginRedirect(loginRequest);
    } catch (error) {
      console.error("Login failed", error);
    }
    handleCallApi();
  };
  const handleCallApi = async () => {
    const account = accounts[0];
    if (!account) return;
    const response = await instance.acquireTokenSilent({
      ...loginRequest,
      account,
    });

    const token = response.accessToken;
    const registrationData = {
      study_group: "string",
      description: "string",
      github_alias: "string",
      tg_alias: "string"
    };

    const res = await fetch("http://localhost/auth/api/v1/entra/login", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify(registrationData)
    });
    const data = await res.text();
    console.log("API response:", data);
  };
  return(
    <button className="border-2 border-(--accent-color-2) rounded-2xl min-h-10 min-w-20 flex justify-center 
    items-center font-[Inter] text-(--secondary-color) bg-(--accent-color-2)/6 text-xs 
    lg:text-xl lg:pt-1.5 lg:pb-1.5 lg:pl-4 lg:pr-4 pl-1 pr-1 cursor-pointer" 
    onClick={() => handleLogin()}>
    Log in using SSO
    </button>
  );
}