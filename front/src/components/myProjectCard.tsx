import { useState } from "react";
import Popup from "reactjs-popup";
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { deleteApplication } from "../utils/backendFetching";


export default function responseCard({props, onDelete}) {
  
  return (
    <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl hover:shadow-md">
      <div className="flex justify-between">
        <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projectName}</h1>
        <div>
          <Popup
            trigger={
              <EditOutlinedIcon className="cursor-pointer"/>
            }
            modal
          >
            {
              // @ts-ignore
              (close) => (
                <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-20 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
                  lmao
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
            width: '70%',
            minWidth: '50%',
            maxWidth: '85%',
            margin: 'auto auto',
            position: 'relative'
          }}>
            {
              // @ts-ignore
              (close) => (
                <div className="modal flex flex-col items-center rounded-2xl bg-(--header-footer-color) py-10 text-(--secondary-color)">
                  <h2 className="font-[Inter] font-bold text-4xl text-(--secondary-color)">Deleting this project is permanent.</h2>
                  <h3 className="font-[Inter] text-lg text-(--secondary-color) pt-1">Are you sure you want to proceed?</h3>
                  <div className="flex justify-between gap-5 pt-3">
                    <button
                      className="cursor-pointer px-4 py-2 bg-(--accent-color-2)/42 focus:outline-none text-(--secondary-color) rounded-2xl text-xl w-fit"
                      onClick={close}
                      >
                      No, go back
                    </button>
                    <button
                      className= "cursor-pointer px-4 py-2 bg-(--primary-color)/42 focus:outline-none text-(--secondary-color) rounded-2xl text-xl w-fit"
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
                      Yes, delete project
                    </button>
                  </div>
                </div>
              )
            }
          </Popup>
        </div>
      </div>
      <div className="flex justify-between">
        <p className="font-[Inter] text-lg">Status: <em className="font-bold">{props.status.at(0) + props.status.slice(1).toLowerCase()}</em></p>
        <Popup
            trigger={
              <div><p>{}</p></div>
            }
            modal
          >
            {
              // @ts-ignore
              (close) => (
                <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-20 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
                  lmao
                </div>
              )
            }
        </Popup>
      </div>
    </div>
  );
}