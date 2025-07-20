import Footer from "../components/footer";
import HomeHeader from "../components/homeHeader";
import 'reactjs-popup/dist/index.css';
import { useEffect, useState } from "react";
import Card from "../components/card";
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { getRoles, getSkills, getCourses, getLikedProjects, getApplications, getNames, getRecs } from "../utils/backendFetching";
import { useNavigate } from "react-router-dom";

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
  person_id: number;
  project: Project;
  status: string;
  created_at: number[];
}

const backendHost = import.meta.env.VITE_BACKEND_HOST

export default function HomeScreen(){
  const navigate = useNavigate();
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [courses, setCourses] = useState<{id: number, name: string}[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const [likedProjects, setLikedProjects] = useState<{id: number, person_id: number, project: Project}[]>([]);
  const [applications, setApplications] = useState<Application[]>([]);

  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    console.log(token);
    if (token){
      getRecs("eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyX2lkIjoxNTgsImlzcyI6InRlYW1zeW5jIiwiaWF0IjoxNzUyOTIzODIyLCJleHAiOjE3NTI5Mjc0MjJ9.dZQiRUqZuIO70G7KaJHBo2jobdYkycx8Jp3-crMFJ3e9x8U09cKN1gyCTqncAyTp").then((result => {
        setProjects(result.projects);
      }));
      getLikedProjects(token).then(setLikedProjects)
      getRoles().then(setRoles);
      getSkills().then(setSkills);
      getCourses(token).then(setCourses);
      getApplications(token).then(setApplications);
    }
  }, []);

  const handleLikeChange = async () => {
    const token = localStorage.getItem("backendToken");
    if (token) {
      const updatedLikedProjects = await getLikedProjects(token);
      setLikedProjects(updatedLikedProjects);
    }
  };

  return (
    <div className="flex flex-col justify-between min-h-screen">
      <div className="mb-32">
          <HomeHeader />
      </div>
      <div className="ml-18">
          <div className="flex pt-3 pb-5 items-center">
            <ArrowBackIosIcon onClick = {() => {navigate("/home")}} sx={{ fontSize: 24 }} className="cursor-pointer text-[color:var(--secondary-color)]"/>
            <p className="font-[Inter] text-(--primary-color) text-xl">
              All projects
            </p>
          </div>
          <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold">
            Recommendations
          </h1>
        </div>        
      <div className="flex flex-col ml-18 mb-5 mr-10">
        <p className="font-[Inter] text-lg mt-5 text-(--primary-color)">
          {projects.length} projects found
        </p>
        {projects.length > 0 && roles.length > 0 && courses.length > 0 && projects.filter((proj) => proj.status == "OPEN" || proj.status == "IN_PROGRESS").map((proj, idx) => (
          <Card
            key={proj.id || idx}
            props={{
              courseName: courses.at(proj.course_id-1)?.name || "",
              description: proj.description,
              id: proj.id,
              projectName: proj.name,
              roles: getNames(proj.role_ids, roles),
              skills: getNames(proj.skill_ids, skills),
              status: proj.status,
              teamLeadId: proj.team_lead_id,
              requiredMembersCount: proj.required_members_count,
              liked: likedProjects.some(likedProj => likedProj.project.id === proj.id),
              isApplied: applications.some(project => project.project.id === proj.id)
            }}
            onLikeChange={handleLikeChange}
          />
        ))}
    </div>
      <Footer/>
    </div>
  );
}