import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer";
import CustomizedHook from "../components/autocompleteInput";
import { useEffect, useState } from "react";
import { useMsal } from "@azure/msal-react";
import { loginRequest } from "../authConfig";
import { useNavigate } from "react-router-dom";


async function getSkills() {
  const skillsUrl = "http://localhost/projects/api/v1/skills";
  try {
    const response = await fetch(skillsUrl);
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content.map((skill) => ({ id: skill.id, name: skill.name }));
  }
  catch (error) {
    console.error(error.message);
  }
}

async function getRoles() {
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

export default function CreateProjectScreen() {
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [selectedRoles, setSelectedRoles] = useState<number[]>([]);
  const [selectedSkills, setSelectedSkills] = useState<number[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    getRoles().then(setRoles);
    getSkills().then(setSkills);
  }, []);

  async function createProject(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const projectUrl = "http://localhost/projects/api/v1/projects";
    if (selectedSkills.length === 0 || selectedRoles.length === 0){
      console.error("Skills or roles empty");
      return;
    }
    const projJson = {
      "course_name": formData.get('courseName'),
      "description": formData.get('description'),
      "name": formData.get('projectName'),
      "skills": selectedSkills,
      "roles": selectedRoles,
      "status": "OPEN"
    };

    const token = localStorage.getItem("backendToken");

    const response = await fetch(projectUrl, {  
      method: 'POST', 
      mode: 'cors', 
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(projJson) 
    });
    if (response.ok) {
      navigate('/home');
    } else {
      alert("Failed to create project");
    }
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
          <CustomizedHook arr={roles} onChange={ids => setSelectedRoles(ids)} />
          <p className="mb-2 mt-5">How many people do you need?</p>
          <input name = "numPeople" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Required skills</p>
          <CustomizedHook arr={skills} onChange={ids => setSelectedSkills(ids)} />
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