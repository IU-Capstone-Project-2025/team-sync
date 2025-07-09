import { useState } from "react";
import Popup from "reactjs-popup";

const backendHost = import.meta.env.VITE_BACKEND_HOST

function truncateString({string, maxLength} : {string: string, maxLength: number}){
  if (string.length >  maxLength){
    return string.substring(0, maxLength-1) + "...";
  }
  else {
    return string;
  }
}

async function sendApplication(projId: number, token){
  const applicationJson = {
    projectId: projId
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
    alert("Application did not work");
    return 0;
  }
  else{
    return 1;
  }
}

export default function Card({props}) {
  const [wasSuccessful, setWasSuccessful] = useState<null | number>(0);

  const handleClick = async () => {
    const token = localStorage.getItem("backendToken");
    const success = await sendApplication(props.id, token);
    setWasSuccessful(success);
  };

  return (
    <Popup
        trigger={
          <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl hover:shadow-md">
            <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projectName}</h1>
            <p className="pb-4 font-[Inter] text-xl max-w-[70%]">{truncateString({ string: props.description, maxLength: 350 })}</p>
            <p className="font-[Inter] text-lg"><em className="font-bold">Available roles:</em> {props.roles.join(", ")}</p>
          </div>
        }
        modal
      >
        {
          // @ts-ignore
          (close) => (
            <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-20 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
              <button className="text-2xl close self-end" onClick={close}> &times;</button>
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
                <span style={{ maxHeight: '10vh', overflowY: 'auto', paddingRight: '4px' }} className="flex flex-row flex-wrap mt-0.5 flex gap-1">
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
                <span style={{ maxHeight: '10vh', overflowY: 'auto', paddingRight: '4px' }} className="flex flex-row flex-wrap mt-0.5 flex gap-1">
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
                  className= "mt-6 px-4 py-2 bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl text-xl w-fit"
                >
                  {wasSuccessful ? "Applied" : "Apply"}
                </button>
              </div>
            </div>
          )
        }
      </Popup>
  );
}