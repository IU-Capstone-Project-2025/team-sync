import Footer from "../components/footer";
import HomeHeader from "../components/homeHeader";
import HomeFilter from "../components/homeFilter";
import { loginRequest } from "../authConfig";
import { useMsal } from "@azure/msal-react";
import { useEffect, useState } from "react";
import { IPublicClientApplication, AccountInfo } from "@azure/msal-browser";
import Card from "../components/card";
interface Project {
  id: number;
  name: string;
  description: string;
  roles: number[];
  skills: number[];
  courseName: string;
  status: "DRAFT" | "OPEN" | "IN_PROGRESS" | "COMPLETE";
}

async function getRoles({instance, accounts} : {instance: IPublicClientApplication, accounts: AccountInfo[]}) {
  const rolesUrl = "http://localhost/projects/api/v1/roles";
  try {
    const response = await fetch(rolesUrl);
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content.map((role) => ({ id: role.id, name: role.name }));
  }
  catch (error) {
    console.error(error.message);
  }
}

async function getProjects() {
  const token = localStorage.getItem("backendToken");
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
function getRoleNames(roleIds: number[] = [], allRoles: {id: number, name: string}[] = []) {
  return Array.isArray(roleIds)
    ? roleIds.map(id => allRoles.find(role => role.id === id)?.name ?? "Unknown")
    : [];
}
export default function HomeScreen(){
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const { instance, accounts } = useMsal();
  useEffect(() => {
    getProjects().then(setProjects);
    getRoles({instance, accounts}).then(setRoles);
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
              {projects.length > 0 && roles.length > 0 && projects.map((proj, idx) => (
                <Card
                  key={proj.id || idx}
                  props={{
                    projName: proj.name,
                    description: proj.description,
                    roles: getRoleNames(proj.roles, roles)
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