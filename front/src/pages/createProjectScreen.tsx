import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer";
import CustomizedHook from "../components/autocompleteInput";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getSkills, getRoles } from "../utils/backendFetching";

const backendHost = import.meta.env.VITE_BACKEND_HOST

export default function CreateProjectScreen() {
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [selectedRoles, setSelectedRoles] = useState<number[]>([]);
  const [selectedSkills, setSelectedSkills] = useState<number[]>([]);
  const navigate = useNavigate();
  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token){
      getSkills().then(setSkills);
      getRoles().then(setRoles);
    }
   }, []);

  async function createProject(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const projectUrl = `${backendHost}/projects/api/v1/projects`;
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
      "status": "OPEN",
      "required_members_count": formData.get('numPeople')
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
      <div className="mb-32">
        <HomeHeader/>
      </div>
      <main className="flex-1 pl-16 pb-10">
        <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">New project</h1>
        <form onSubmit={createProject}>
          <p className="mb-2">Name</p>
          <input name = "projectName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Description</p>
          <textarea name = "description" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-30 min-w-150 p-1 text-(--secondary-color) font-[Inter] text-md"/>
          <p className="mb-2">Course</p>
          <input name = "courseName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Required roles</p>
          <CustomizedHook
            arr={roles}
            value={roles.filter(role => selectedRoles.includes(role.id))}
            onChange={items => setSelectedRoles(items.map(item => item.id))}
          />
          <p className="mb-2 mt-5">How many people do you need?</p>
          <input name = "numPeople" required className = "[&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none [appearance:textfield] focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 w-25 p-1 text-(--secondary-color) font-[Inter] text-md" type="number" />
          <p className="mb-2">Required skills</p>
          <div className="relative">
            <CustomizedHook
              arr={skills}
              value={skills.filter(skill => selectedSkills.includes(skill.id))}
              onChange={items => setSelectedSkills(items.map(item => item.id))}
            />
          </div>
          <button className = "bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl my-5" type="submit">
            Add project
          </button>
        </form>
      </main>
      <Footer/>
    </div>
  );
}