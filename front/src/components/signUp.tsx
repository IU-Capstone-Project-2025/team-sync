import {useMsal} from "@azure/msal-react"
import {loginRequest} from "../authConfig"
export default function SignUpButton(){
  const {instance, accounts}  = useMsal();
  const handleLogin = async () => {
    try {
      await instance.loginRedirect(loginRequest);
    } catch (error) {
      console.error("Login failed", error);
    }
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