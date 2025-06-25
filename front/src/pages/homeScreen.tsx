import Footer from "../components/footer";
import HomeHeader from "../components/homeHeader";
import HomeFilter from "../components/homeFilter";
import { loginRequest } from "../authConfig";
import { useMsal } from "@azure/msal-react";
import { useEffect, useState } from "react";
import { IPublicClientApplication, AccountInfo } from "@azure/msal-browser";
import Card from "../components/card";

async function getProjects({instance, accounts} : {instance: IPublicClientApplication, accounts: AccountInfo[]}) {
  const account = accounts[0];
  const response = await instance.acquireTokenSilent({
    ...loginRequest,
    account,
  });
  const token = response.accessToken;
  const projectsUrl = "http://localhost/projects/api/v1/projects?page=0&size=100";
  try {
    const response = await fetch(projectsUrl, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content;
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}
async function login({instance, accounts} : {instance: IPublicClientApplication, accounts: AccountInfo[]}) {
  const account = accounts[0];
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

  try {
    const res = await fetch("http://localhost:8080/entra/login", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
    });
    const loginResult = await res.json();
    if (loginResult.success && loginResult.data && loginResult.data.access_token) {
      localStorage.setItem("backendToken", loginResult.data.access_token);
    } else if (res.status === 405) {
      const regRes = await fetch("http://localhost:8080/entra/registration/student", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(registrationData)
      });
      const regData = await regRes.json();
      if (regData.success && regData.data && regData.data.access_token) {
        localStorage.setItem("backendToken", regData.data.access_token);
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
export default function HomeScreen(){
  const { instance, accounts } = useMsal();
  const [projects, setProjects] = useState<any[]>([]);

  useEffect(() => {
    const demoProjects = JSON.parse(localStorage.getItem("demoProjects") || "[]");
    setProjects(demoProjects);
  }, []);

  return (
    <div className="flex flex-col justify-between min-h-screen">
      <HomeHeader />
      <div className="flex flex-col px-18">
          <p className="font-[Inter] text-(--primary-color) text-xl pb-5">
            All projects
          </p>
          <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold">
            Projects
          </h1>
          <div className="flex flex-row">
            <HomeFilter/>
            <div className="flex flex-col ml-15">
              <p className="font-[Inter] text-lg mt-5 text-(--primary-color)">
                {projects.length} projects found
              </p>
              {projects.map((proj, idx) => (
                <Card
                  key={proj.id || idx}
                  props={{
                    projName: proj.name || proj.projName,
                    description: proj.description,
                    roles: Array.isArray(proj.roleNames) ? proj.roleNames : (Array.isArray(proj.roles) ? proj.roles.map(role => typeof role === 'string' ? role : (role.name || role)).join(', ').split(', ') : []),
                    numPeople: proj.numPeople || (proj.members ? proj.members.length : 0)
                  }}
                />
              ))}
            </div>
          </div>
      </div>
      <Footer/>
    </div>
  );
}