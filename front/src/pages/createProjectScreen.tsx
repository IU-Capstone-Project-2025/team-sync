import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer";
import CustomizedHook from "../components/autocompleteInput";
import { useEffect, useState } from "react";
import { useMsal } from "@azure/msal-react";
import { loginRequest } from "../authConfig";
import { useNavigate } from "react-router-dom";


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
    return json.data.content.map((skill) => ({ id: skill.id, name: skill.name }));
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
    return json.data.content.map((role) => ({ id: role.id, name: role.name }));
  }
  catch (error) {
    console.error(error.message);
  }
}

export default function CreateProjectScreen() {
  const { instance, accounts } = useMsal();
  const [rolesArray, setRolesArray] = useState<{id: number, name: string}[]>([]);
  const [skillsArray, setSkillsArray] = useState<{id: number, name: string}[]>([]);
  const [selectedRoleNames, setSelectedRoleNames] = useState<string[]>([]);
  const [selectedSkillNames, setSelectedSkillNames] = useState<string[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    getRoles(instance, accounts).then(setRolesArray);
    getSkills(instance, accounts).then(setSkillsArray);
  }, [instance, accounts]);

  async function createProject(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    if (selectedSkillNames.length === 0 || selectedRoleNames.length === 0){
      console.error("Skills or roles empty");
      return;
    }
    const skills = skillsArray.filter(skill => selectedSkillNames.includes(skill.name)).map(skill => skill.id);
    const roles = rolesArray.filter(role => selectedRoleNames.includes(role.name)).map(role => role.id);
    // Save both role IDs and names for display
    const projJson = {
      id: Date.now(), // unique id for demo
      name: formData.get('projectName'),
      course_name: formData.get('courseName'),
      description: formData.get('description'),
      project_link: formData.get('projectLink'),
      status: "OPEN",
      skills,
      roles,
      roleNames: selectedRoleNames, // <-- add this line
      numPeople: formData.get('numPeople') // ensure numPeople is saved
    };
    // Hardcoded: Save project to localStorage for HomeScreen
    // Get current projects from localStorage, add new, and save back
    const current = JSON.parse(localStorage.getItem("demoProjects") || "[]");
    current.push(projJson);
    localStorage.setItem("demoProjects", JSON.stringify(current));
    navigate("/");
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
          <CustomizedHook arr={rolesArray.map(r => r.name)} onChange={setSelectedRoleNames}/>
          <p className="mb-2 mt-5">How many people do you need?</p>
          <input name = "numPeople" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Required skills</p>
          <CustomizedHook arr={skillsArray.map(s => s.name)} onChange={setSelectedSkillNames}/>
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