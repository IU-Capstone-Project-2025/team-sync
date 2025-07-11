import React, { useEffect, useState } from "react";
import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer"
import Pill from "../components/pill";
import AddIcon from '@mui/icons-material/Add';
import ResponseCard from "../components/responseCard"
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { useNavigate } from "react-router-dom";
import Card from "../components/card";
interface Project {
  course_id: number;
  description: string;
  id: number;
  name: string;
  skill_ids: number[];
  role_ids: number[];
  status: "DRAFT" | "OPEN" | "IN_PROGRESS" | "COMPLETE";
  team_lead_id: number;
  required_members_count: number;
}
interface Application {
  application_id: number;
  project: Project;
  status: string;
  created_at: number[];
}

const backendHost = import.meta.env.VITE_BACKEND_HOST

async function getRoles(token: string) {
  const rolesUrl = `${backendHost}/projects/api/v1/roles`;
  try {
    const response = await fetch(rolesUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
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

async function getSkills(token: string) {
  const rolesUrl = `${backendHost}/projects/api/v1/skills`;
  try {
    const response = await fetch(rolesUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
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
async function getLikedProjects(token: string) {
  const projectsUrl = `${backendHost}/projects/api/v1/favourite/my`;
  try {
    const response = await fetch(projectsUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

async function likeProject(projId: number, token: string){
  const projectJson = {
    project_id: projId
  };
  const applicationUrl = `${backendHost}/projects/api/v1/favourite`;
  const response = await fetch(applicationUrl, {  
    method: 'POST', 
    mode: 'cors', 
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(projectJson) 
  });
  if (!response.ok){
    alert("Like operation failed");
    return false;
  }
  return true;
}

async function unlikeProject(projId: number, token: string){
  const applicationUrl = `${backendHost}/projects/api/v1/favourite/${projId}`;
  const response = await fetch(applicationUrl, {  
    method: 'DELETE', 
    mode: 'cors', 
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    }
  });
  if (!response.ok){
    alert("Unlike operation failed");
    return false;
  }
  return true;
}

function getNames(ids: number[] = [], all: {id: number, name: string}[] = []) {
  const names = ids.map(id => all.find(obj => obj.id === id)?.name ?? "Unknown");
  return names;
}

export default function ResponseScreen(){
  const navigate = useNavigate();
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [likes, setLikes] = useState<{id: number, person_id: number, project: Project}[]>([]);
  const [refreshKey, setRefreshKey] = useState(0);
  const handleLikeChange = async () => {
    const token = localStorage.getItem("backendToken");
    if (token) {
      const updatedLikedProjects = await getLikedProjects(token);
      setLikes(updatedLikedProjects);
    }
  };
  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token){
      getRoles(token).then(setRoles);
      getSkills(token).then(setSkills);
    }
  }, []);
  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token){
      getLikedProjects(token).then(result => setLikes(result));
    }
  }, [refreshKey]);
  return(
    <div className="flex flex-col min-h-screen">
      <HomeHeader />
      <div className="flex flex-col flex-1"> 
        <div className="flex flex-col px-18">
          <div className="flex flex-row justify-start items-center py-5"> 
            <ArrowBackIosIcon onClick = {() => {navigate(-1)}} sx={{ fontSize: 24 }} className="cursor-pointer text-[color:var(--secondary-color)]"/>
            <h2 className="font-[Inter] text-(--primary-color) text-xl">
              All projects
            </h2>
          </div>
          <div className="flex flex-row gap-4">
            <Pill type="Active" number={0}/>
          </div>
          {likes.length > 0 && roles.length > 0 && likes.map((project) => project.project).map((proj) => (
            <Card
              key={proj.id}
              props={{
                courseName: likes.at(proj.course_id-1)?.project.name || "",
                description: proj.description,
                id: proj.id,
                projectName: proj.name,
                roles: getNames(proj.role_ids, roles),
                skills: getNames(proj.skill_ids, skills),
                status: proj.status,
                teamLeadId: proj.team_lead_id,
                requiredMembersCount: proj.required_members_count,
                liked: true
              }}
              onLikeChange={handleLikeChange}
            />
          ))}
        </div>
      </div>
      <Footer />
    </div>
  );
}