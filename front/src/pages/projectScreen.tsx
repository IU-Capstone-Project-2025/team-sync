import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer"
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import Pill from "../components/pill";
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import MyCard from "../components/myProjectCard";
import { getMyProjects } from "../utils/backendFetching"

const backendHost = import.meta.env.VITE_BACKEND_HOST
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
export default function ProjectScreen() {
  const navigate = useNavigate();
  const [projects, setProjects] = useState<Project[]>([]);
  const [refreshKey, setRefreshKey] = useState(0);
  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token){
      getMyProjects(token).then(result => setProjects(result.projects));
    }
  }, [refreshKey]);

  const handleProjectUnlike = () => {
    setRefreshKey(prev => prev + 1);
  };
  return (
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
            <Pill type="Drafts" number={0}/>
            <Pill type="Completed" number={0}/>
          </div>
          {projects.length > 0 ? (
              projects.map((proj) => (
                <MyCard
                  key={proj.id}
                  props={{
                    courseName: proj.course_name,
                    description: proj.description,
                    id: proj.id,
                    projectName: proj.name,
                    status: proj.status,
                    teamLeadId: proj.team_lead_id
                  }}
                  onDelete={handleProjectUnlike}
                />
              ))
            ) : (
              <div className="flex flex-1 flex-col items-center justify-center">
                <img className="w-[5%]" src="./sadFace.jpg" alt="A sad face" />
                <p className="font-[Inter] text-xl text-(--primary-color)">
                  You don't have any projects
                </p>
                <button
                  className="flex flex-row items-center px-4 mt-4 border-(--accent-color-2) border-2 rounded-xl text-(--secondary-color) cursor-pointer gap-x-2"
                  onClick={() => navigate("/projects/create")}
                >
                  <AddIcon fontSize="large" />
                  <p className="font-[Inter] text-xl leading-none m-0 pl-4">
                    Create a project
                  </p>
                </button>
              </div>
            )}
          </div>
      </div>
      <Footer/>
    </div>
  );
}