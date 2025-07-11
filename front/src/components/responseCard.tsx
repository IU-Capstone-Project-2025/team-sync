import { useState } from "react";
import Popup from "reactjs-popup";
import InfoOutlineIcon from '@mui/icons-material/InfoOutline';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';

function truncateString({string, maxLength} : {string: string, maxLength: number}){
  if (string.length >  maxLength){
    return string.substring(0, maxLength-1) + "...";
  }
  else {
    return string;
  }
}

async function deleteApplication(token: string, applicationId: number) : Promise<boolean>{
  const applicationUrl = "/projects/api/v1/applications/" + applicationId.toString();
    const appJson = {
      applicationId: applicationId
    };
    try {
      const response = await fetch(applicationUrl, {  
        method: 'DELETE', 
        mode: 'cors', 
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(appJson) 
      });
      return response.ok;
    }
    catch (error){
      return false;
    }
}

export default function responseCard({props, onDelete}) {

  return (
    <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl hover:shadow-md">
      <div className="flex justify-between">
        <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projectName}</h1>
        <div>
          <Popup
            trigger={
              <InfoOutlineIcon className="cursor-pointer"/>
            }
            modal
          >
            {
              // @ts-ignore
              (close) => (
                <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-20 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
                  <button className="cursor-pointer text-2xl close self-end" onClick={close}> &times;</button>
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
                    <p className="font-[Inter] text-xl text-(--secondary-color)">{props.projStatus.at(0) + props.projStatus.slice(1).toLowerCase()}</p>
                  </div>
                </div>
              )
            }
          </Popup>
          <Popup
          trigger={
            <DeleteOutlineIcon className="cursor-pointer"/>
          }
          modal
          contentStyle={{
            width: '40%',
            margin: 'auto auto',
            position: 'relative'
          }}>
            {
              // @ts-ignore
              (close) => (
                <div className="modal flex flex-col items-center rounded-2xl bg-(--header-footer-color) py-10 text-(--secondary-color)">
                  <h2 className="font-[Inter] font-bold text-4xl text-(--secondary-color)">Cancel project application?</h2>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color) pt-1">You're about to cancel your application. Are you sure?</h3>
                  <div className="flex justify-between gap-5 pt-3">
                    <button
                      className="cursor-pointer px-4 py-2 bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl text-xl w-fit"
                      onClick={close}
                      >
                      No, go back
                    </button>
                    <button
                      className= "cursor-pointer px-4 py-2 bg-(--primary-color)/42 text-(--secondary-color) rounded-2xl text-xl w-fit"
                      onClick={() => {
                        const token = localStorage.getItem("backendToken");
                        if (token){
                          deleteApplication(token, props.applicationId).then(success => {
                            if (success) {
                              close();
                              onDelete();
                            }
                          })
                          .catch(error => console.error("Delete error:", error));
                        }}}
                      >
                      Yes, cancel it
                    </button>
                  </div>
                </div>
              )
            }
          </Popup>
        </div>
      </div>
      <p className="font-[Inter] text-lg">Status: <em className="font-bold">{props.appStatus.at(0) + props.appStatus.slice(1).toLowerCase()}</em></p>
    </div>
  );
}