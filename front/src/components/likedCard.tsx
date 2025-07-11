import { useState } from "react";
import Popup from "reactjs-popup";
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';

async function deleteLike(token: string, applicationId: number) : Promise<boolean>{
  const likeUrl = "/projects/api/v1/favourite/" + applicationId.toString();
    const appJson = {
      applicationId: applicationId
    };
    try {
      const response = await fetch(likeUrl, {  
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

export default function likedCard({props, onDelete}) {

  return (
    <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl hover:shadow-md">
      <div className="flex justify-between">
        <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projectName}</h1>
        <div>
          <Popup
            trigger={
              <FavoriteOutlinedIcon className="cursor-pointer"/>
            }
            modal
          >
            {
              // @ts-ignore
              (close) => (
                <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-20 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
                </div>
              )
            }
          </Popup>
          <DeleteOutlineIcon className="cursor-pointer"
          onClick={() => {
            const token = localStorage.getItem("backendToken");
            if (token){
              deleteLike(token, props.applicationId).then(success => {
                if (success) {
                  onDelete();
                }
              })
              .catch(error => console.error("Delete error:", error));
            }}}/>
        </div>
      </div>
      <p className="font-[Inter] text-lg">Status: <em className="font-bold">{props.status.at(0) + props.status.slice(1).toLowerCase()}</em></p>
    </div>
  );
}