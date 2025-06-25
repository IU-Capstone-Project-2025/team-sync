import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer";
import CustomizedHook from "../components/autocompleteInput";
import { useEffect, useState } from "react";
import { useMsal } from "@azure/msal-react";
import { loginRequest } from "../authConfig";


async function getSkills(instance, accounts) {
  const account = accounts[0];
  const response = await instance.acquireTokenSilent({
    ...loginRequest,
    account,
  });
  const token = response.accessToken;
  const skillsUrl = "http://localhost/projects/api/v1/skills";
  try {
    const response = await fetch(skillsUrl, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content.map((skill) => skill.name);
  }
  catch (error) {
    console.error(error.message);
  }
}

async function getRoles(instance, accounts) {
  const account = accounts[0];
  const response = await instance.acquireTokenSilent({
    ...loginRequest,
    account,
  });
  const token = response.accessToken;
  const rolesUrl = "http://localhost/projects/api/v1/roles";
  try {
    const response = await fetch(rolesUrl, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content.map((role) => role.name);
  }
  catch (error) {
    console.error(error.message);
  }
}

export default function CreateProjectScreen() {
  const { instance, accounts } = useMsal();
  const [rolesArray, setRolesArray] = useState<string[]>([]);
  const [skillsArray, setSkillsArray] = useState<string[]>([]);
  const [selectedRoles, setSelectedRoles] = useState<string[]>([]);
  const [selectedSkills, setSelectedSkills] = useState<string[]>([]);
  // const [rolesArray] = useState(['Role1', 'Role2']);
  // const [skillsArray] = useState(['Skill1', 'Skill2']);
  useEffect(() => {
    getRoles(instance, accounts).then(setRolesArray);
    getSkills(instance, accounts).then(setSkillsArray);
  }, [instance, accounts]);
  async function createProject(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const projectUrl = "http://localhost/projects/api/v1/projects";
    if (selectedSkills.length === 0 || selectedRoles.length === 0){
      console.error("Skills or roles empty");
      return;
    }
    const projJson = {
      "course_name": formData.get('projectName'),
      "description": formData.get('description'),
      "project_link": formData.get('projectLink'),
      "status": "OPEN",
      "skills": selectedSkills,
      "roles": selectedRoles
    };

    const account = accounts[0];
    const response = await instance.acquireTokenSilent({
      ...loginRequest,
      account,
    });
    const token = response.accessToken;

    fetch(projectUrl, {  
      method: 'POST', 
      mode: 'cors', 
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(projJson) 
    })
  }
  return (
    <div className="flex flex-col min-h-screen">
      <HomeHeader/>
      <div className="pl-16 pt-10 flex-1">
        <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">New project</h1>
        <form onSubmit={createProject}>
          <p className="mb-2">Name</p>
          <input name = "projectName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Description</p>
          <input name = "description" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Course</p>
          <input name = "courseName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Required roles</p>
          <CustomizedHook arr={rolesArray} onChange = {setSelectedRoles}/>
          <p className="mb-2 mt-5">How many people do you need?</p>
          <input name = "numPeople" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Required skills</p>
          <CustomizedHook arr={skillsArray} onChange = {setSelectedSkills}/>
          <p className="mb-2 mt-5">Project link</p>
          <input name = "projectLink" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="url" />
          <button className = "bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl mb-5" type="submit">
            Add project
          </button>
        </form>
      </div>
      <Footer/>
    </div>
  );
}