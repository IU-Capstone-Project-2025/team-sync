import React, { useEffect, useState } from "react";
import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer"
import Pill from "../components/pill";
import AddIcon from '@mui/icons-material/Add';
import ResponseCard from "../components/responseCard"
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { useNavigate } from "react-router-dom";
import { getRoles, getSkills, getApplications, getNames } from "../utils/backendFetching";
interface Project {
  course_name: string;
  description: string;
  id: number;
  name: string;
  skill_ids: number[];
  role_ids: number[];
  status: "DRAFT" | "OPEN" | "IN_PROGRESS" | "COMPLETE";
  team_lead_id: number;
}
interface Application {
  application_id: number;
  project: Project;
  status: string;
  created_at: number[];
}

const backendHost = import.meta.env.VITE_BACKEND_HOST

export default function ResponseScreen(){
  const navigate = useNavigate();
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [applications, setApplications] = useState<Application[]>([]);
  const [refreshKey, setRefreshKey] = useState(0);

  const handleApplicationDeleted = () => {
    setRefreshKey(prev => prev + 1);
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
      getApplications(token).then(result => setApplications(result.applications));
    }
  }, [refreshKey]);
  return(
    <div className="flex flex-col min-h-screen">
      <div className="mb-32">
        <HomeHeader />
      </div>
      <div className="flex flex-col flex-1"> 
        <div className="flex flex-col px-18">
          <div className="flex flex-row justify-start items-center py-5"> 
            <ArrowBackIosIcon onClick = {() => {navigate("/home")}} sx={{ fontSize: 24 }} className="cursor-pointer text-[color:var(--secondary-color)]"/>
            <h2 className="font-[Inter] text-(--primary-color) text-xl">
              All projects
            </h2>
          </div>
          <div className="flex flex-row gap-4">
            <Pill type="Active" number={0}/>
          </div>
          {applications.length && applications.map((app) => (
            <ResponseCard
              key={app.application_id}
              props={{
                courseName: app.project.course_name,
                description: app.project.description,
                applicationId: app.application_id,
                projectName: app.project.name,
                roles: getNames(app.project.role_ids, roles),
                skills: getNames(app.project.skill_ids, skills),
                appStatus: app.status,
                projStatus: app.project.status,
                teamLeadId: app.project.team_lead_id
              }}
              onDelete = {handleApplicationDeleted}
            />
            ))}
        </div>
      </div>
      <Footer/>
    </div>
  );
}