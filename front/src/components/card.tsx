import React, { useEffect, useState } from "react";
import Popup from "reactjs-popup";
import PeopleIcon from '@mui/icons-material/People';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FavoriteIcon from '@mui/icons-material/Favorite';

interface CardProps {
  props: {
    courseName: string;
    description: string;
    id: number;
    projectName: string;
    roles: string[];
    skills: string[];
    status: string;
    teamLeadId: number;
    requiredMembersCount: number;
    liked: boolean;
    isApplied: boolean;
  };
  onLikeChange?: (projectId: number, isLiked: boolean) => void;
}

const backendHost = import.meta.env.VITE_BACKEND_HOST

function truncateString({string, maxLength} : {string: string, maxLength: number}){
  if (string.length >  maxLength){
    return string.substring(0, maxLength-1) + "...";
  }
  else {
    return string;
  }
}

async function sendApplication(projId: number, token: string){
  const applicationJson = {
    project_id: projId
  };
  const applicationUrl = `${backendHost}/projects/api/v1/applications`;
  const response = await fetch(applicationUrl, {  
    method: 'POST', 
    mode: 'cors', 
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(applicationJson) 
  });
  if (!response.ok){
    const json = await response.json();
    alert(json.error.message);
    return false;
  }
  else{
    return true;
  }
}

async function deleteApplicationByProjectId(projId: number, token: string){
  const applicationUrl = `${backendHost}/projects/api/v1/applications/project/${projId}`;
  const response = await fetch(applicationUrl, {  
    method: 'DELETE', 
    mode: 'cors', 
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
  });
  if (!response.ok){
    const json = await response.json();
    alert(json.error.message);
    return true;
  }
  else{
    return false;
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

export default function Card({props, onLikeChange}: CardProps) {
  const [applied, setApplied] = useState<boolean>(props.isApplied);
  const [isLiked, setIsLiked] = useState(props.liked);

  useEffect(() => {
    setIsLiked(props.liked);
  }, [props.liked]);

  const handleClick = async () => {
    const token = localStorage.getItem("backendToken");
    if (token) {
      if (applied){
        const success = await deleteApplicationByProjectId(props.id, token);
        setApplied(success);
      }
      else{
        const success = await sendApplication(props.id, token);
        setApplied(success);
      }
    }
  };
  const handleLikeClick = async (e: React.MouseEvent) => {
    e.stopPropagation();
    const token = localStorage.getItem('backendToken');
    if (!token) return;

    try {
      setIsLiked(!isLiked);
      
      const success = isLiked 
        ? await unlikeProject(props.id, token)
        : await likeProject(props.id, token);

      if (!success) {
        setIsLiked(isLiked);
        alert(`Failed to ${isLiked ? 'unlike' : 'like'} project`);
      } else {
        if (onLikeChange) {
          onLikeChange(props.id, !isLiked);
        }
      }
    } catch (error) {
      setIsLiked(isLiked);
      console.error("Error updating like:", error);
    }
  };


  return (
    <div className="relative text-(--secondary-color)">
      <div className="absolute right-5 top-7.5 z-50">
        {isLiked ? (
          <FavoriteIcon
            onClick={handleLikeClick}
            className="cursor-pointer text-(--primary-color) hover:text-(--secondary-color)"
          />
        ) : (
          <FavoriteBorderIcon
            onClick={handleLikeClick}
            className="cursor-pointer hover:text-(--primary-color)"
          />
        )}
      </div>
      <Popup
        trigger={
          <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl hover:shadow-md cursor-pointer">
            <div className="flex justify-between align-baseline">
              <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projectName}</h1>
            </div>
            <p className="pb-4 font-[Inter] text-xl max-w-[70%]">
              {truncateString({ string: props.description, maxLength: 350 })}
            </p>
            <div className="flex justify-between align-baseline">
              <p className="font-[Inter] text-lg">
                <em className="font-bold">Available roles:</em> {props.roles.join(", ")}
              </p>
              <div className="flex justify-between items-center gap-1">
                <PeopleIcon />
                <p className="font-[Inter] text-lg">
                  <em className="font-bold">{props.requiredMembersCount}</em>
                </p>
              </div>
            </div>
          </div>
        }
        modal
      >
          {
            // @ts-ignore
            (close) => (
              <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-20 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
                <button className="text-2xl close self-end cursor-pointer" onClick={close}> &times;</button>
                <h2 className="font-[Inter] font-bold text-4xl text-(--secondary-color)">{props.projectName}</h2>
                <div>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color)/48">PROJECT DESCRIPTION</h3>
                  <p style={{ maxHeight: '20vh', overflowY: 'auto', paddingRight: '4px' }} className="font-[Inter] text-xl text-(--secondary-color)">{props.description}</p>
                </div>
                <div>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color)/48">COURSE</h3>
                  <p className="font-[Inter] text-xl text-(--secondary-color)">{props.courseName}</p>
                </div>
                <div>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color)/48">REQUIRED ROLES</h3>
                  <span style={{ maxHeight: '10vh', overflowY: 'auto', paddingRight: '4px' }} className="flex-row flex-wrap mt-0.5 flex gap-1">
                    {props.roles.length > 0 && props.roles.map((role) => {
                      return (
                        <button
                          key={role}
                          type="button"
                          className=
                            "inline-flex flex-row items-center font-[Inter] border-(--secondary-color)/75 text-(--secondary-color) border-3 rounded-lg px-2 py-1 w-fit "
                        >
                          {role}
                        </button>
                      );     
                    })}
                  </span>
                </div>
                <div>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color)/48">REQUIRED SKILLS</h3>
                  <span style={{ maxHeight: '10vh', overflowY: 'auto', paddingRight: '4px' }} className="flex flex-row flex-wrap mt-0.5 gap-1">
                    {props.skills.length > 0 && props.skills.map((skill) => {
                      return (
                        <button
                          key={skill}
                          type="button"
                          className=
                            "inline-flex flex-row items-center font-[Inter] border-(--secondary-color)/75 text-(--secondary-color) border-3 rounded-lg px-2 py-1 w-fit "
                        >
                          {skill}
                        </button>
                      );     
                    })}
                  </span>
                </div>
                <div>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color)/48">PROJECT STATUS</h3>
                  <p className="font-[Inter] text-xl text-(--secondary-color)">{props.status.at(0) + props.status.slice(1).toLowerCase()}</p>
                </div>
                <div>
                  <button 
                    onClick={async () => {
                      handleClick();
                    }}
                    className= {(applied ? "bg-(--secondary-color)/42 " : "bg-(--accent-color-2)/42 ") + "cursor-pointer mt-6 px-4 py-2 text-(--secondary-color) rounded-2xl text-xl w-fit"}
                  >
                    {applied ? "Applied" : "Apply"}
                  </button>
                </div>
              </div>
            )
          }
        </Popup>
      </div>
  );
}