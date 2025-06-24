import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer"
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import Pill from "../components/pill";
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from "react-router-dom";
export default function ProjectScreen() {
  const navigate = useNavigate();
  return (
    <div className="flex flex-col min-h-screen">
      <HomeHeader />
      <div className="flex flex-col flex-1"> {/* This fills the space between header and footer */}
        <div className="flex flex-col px-18">
          <div className="flex flex-row justify-start items-center py-5"> 
            <ArrowBackIosIcon sx={{ fontSize: 24 }} className="text-[color:var(--secondary-color)]"/>
            <p className="font-[Inter] text-(--primary-color) text-xl">
              All projects
            </p>
          </div>
          <div className="flex flex-row gap-4">
            <Pill type="Active" number={0}/>
            <Pill type="Drafts" number={0}/>
            <Pill type="Completed" number={0}/>
          </div>
        </div>
        <div className="flex flex-1 flex-col items-center justify-center">
          <img className="w-[5%]" src="./sadFace.jpg" alt="A sad face" />
          <p className="font-[Inter] text-xl text-(--primary-color)">
            You don't have any projects
          </p>
          <button
            className="flex flex-row items-center px-4 mt-4 border-(--accent-color-2) border-2 rounded-xl text-(--secondary-color) cursor-pointer gap-x-2"
            onClick={() => navigate("/create_project")}
          >
            <AddIcon fontSize="large" />
            <p className="font-[Inter] text-xl leading-none m-0 pl-4">
              Create a project
            </p>
          </button>
        </div>
      </div>
      <Footer />
    </div>
  );
}