import { useEffect, useState } from "react";
import Popup from "reactjs-popup";
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import { deleteProject, getPersonById, getProjectApplications, getSkills, getRoles, approveApplication, rejectApplication, returnApplication } from "../utils/backendFetching";
import CustomizedHook from "./autocompleteInput";
import { useNavigate } from "react-router-dom";
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';

const backendHost = import.meta.env.VITE_BACKEND_HOST

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

interface Person {
  type: "STUDENT" | "TEACHER";
  person: {
    id: number;
    name: string;
    surname: string;
    email: string;
  }
  studentProfile: {
    studyGroup: {
      id: number;
      name: string;
    }
    description: string;
    githubAlias: string;
    tgAlias: string;
    skills: number[];
    roles: number[];
  }
  professorProfile: {
    tgAlias: string;
  }
}

export default function myProjCard({props, onDelete}) {
  const navigate = useNavigate();
  const [applications, setApplications] = useState<Application[]>([]);
  const [applicants, setApplicants] = useState<Person[]>([]);
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [selectedRoles, setSelectedRoles] = useState<number[]>([]);
  const [selectedSkills, setSelectedSkills] = useState<number[]>([]);
  const [render, setRender] = useState<number>(0);

  useEffect(() => {
    getSkills().then(setSkills);
    getRoles().then(setRoles);
  }, []);
  useEffect(() => {
      const token = localStorage.getItem("backendToken");
      if (token){
        getProjectApplications(token, props.id).then(setApplications);
        setSelectedRoles(props.role_ids);
        setSelectedSkills(props.skill_ids);
      }
    }, [render]);

  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token && applications.length > 0) {
      Promise.all(applications.map(app =>
        getPersonById(token, app.person_id)
      )).then(results => {
        setApplicants(results);
      });
    }
  }, [applications]);

    async function editProject(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const projectUrl = `${backendHost}/projects/api/v1/projects`;
    if (selectedSkills.length === 0 || selectedRoles.length === 0){
      console.error("Skills or roles empty");
      return;
    }
    const projJson = {
      "course_name": formData.get('courseName'),
      "description": formData.get('description'),
      "name": formData.get('projectName'),
      "skills": selectedSkills,
      "roles": selectedRoles,
      "status": "OPEN",
      "required_members_count": formData.get('numPeople')
    };
    const token = localStorage.getItem("backendToken");

    const response = await fetch(projectUrl, {  
      method: 'PUT', 
      mode: 'cors', 
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(projJson) 
    });
    if (response.ok) {
      navigate('/home');
    } else {
      alert("Failed to edit project");
    }
  }

  return (
    <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl hover:shadow-md">
      <div className="flex justify-between">
        <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.name}</h1>
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
                <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-10 pr-7 py-5 pb-10 gap-2 text-(--secondary-color)">
                  <button className="text-3xl close self-end cursor-pointer" onClick={close}> &times;</button>
                  <form onSubmit={editProject} className="w-full">
                    <h1 className="font-[Manrope] text-4xl font-extrabold mb-3">Edit project</h1>
                    <p className="mb-1">Name</p>
                    <input name = "projectName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-2 border-(--secondary-color) border-2 rounded-2xl min-h-10 w-[30%] p-1 text-(--secondary-color) font-[Inter] text-md" type="query" value={props.name}/>
                    <p className="mb-1">Description</p>
                    <textarea name = "description" required className = "focus:border-(--accent-color-2) focus:outline-none mb-2 border-(--secondary-color) border-2 rounded-2xl min-h-30 w-[80%] p-1 text-(--secondary-color) font-[Inter] text-md" value={props.description}/>
                    <p className="mb-1">Course</p>
                    <input name = "courseName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-2 border-(--secondary-color) border-2 rounded-2xl min-h-10 w-[50%] p-1 text-(--secondary-color) font-[Inter] text-md" type="query" value={props.course_name}/>
                    <p className="mb-1">Required roles</p>
                    <CustomizedHook
                      arr={roles}
                      value={roles.filter(role => selectedRoles.includes(role.id))}
                      onChange={items => setSelectedRoles(items.map(item => item.id))}
                    />
                    <p className="mb-1 mt-2">How many people do you need?</p>
                    <input name = "numPeople" required className = "[&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none [appearance:textfield] focus:border-(--accent-color-2) focus:outline-none mb-3 border-(--secondary-color) border-2 rounded-2xl min-h-10 w-[30%] p-1 text-(--secondary-color) font-[Inter] text-md" 
                    type="number" value ={props.required_members_count}/>
                    <p className="mb-1">Required skills</p>
                    <div className="relative">
                      <CustomizedHook
                        arr={skills}
                        value={skills.filter(skill => selectedSkills.includes(skill.id))}
                        onChange={items => setSelectedSkills(items.map(item => item.id))}
                      />
                    </div>
                    <button className = "bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl mt-5" type="submit">
                      Update project
                    </button>
                  </form>
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
                          deleteProject(token, props.id).then(success => {
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
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-2">
          <p className="font-[Inter] text-lg">Status: <em className="font-bold">{props.status.at(0) + props.status.slice(1).toLowerCase()}</em></p>
        </div>
        {
          applications.length > 0 ?
          <Popup
              trigger={
                <div><p className="cursor-pointer">{applications.length} applications</p></div>
              }
              modal
            >
              {
                // @ts-ignore
                (close) => (
                  <div className="modal flex flex-col items-start rounded-2xl bg-(--header-footer-color) pl-15 pr-12.5 py-12.5 gap-2 text-(--secondary-color)">
                    <h1 className="font-[Inter] text-5xl font-bold mb-5">Responses</h1>
                    {applications.length > 0 && applications.map((application) => {
                      const applicant = applicants.find(a => a && a.person && a.person.id === application.person_id);
                      const token = localStorage.getItem("backendToken");
                      return (
                        <div className="flex w-full justify-between" key={application.application_id}>
                          <div className="flex justify-between items-center bg-[var(--primary-color)]/25 w-[80%] p-2 rounded-xl">
                            <p className="text-2xl">
                              {applicant ? `${applicant.person.name} ${applicant.person.surname}` : "Unknown"}
                            </p>
                            <KeyboardArrowDownIcon 
                              sx={{ fontSize: 32 }}
                              className="cursor-pointer"
                              onClick={() => applicant && applicant.person && navigate(`/users/${applicant.person.id}`)}
                            />
                          </div>
                          <div className="flex items-center gap-3">
                              {application.status === "PENDING" && (
                                <>
                                  <button
                                    className="text-3xl font-bold text-(--accent-color-2) cursor-pointer"
                                    onClick={async () => {
                                      await approveApplication(token, application.project.id, application.application_id);
                                      setRender(render+1);}
                                    }
                                    title="Approve"
                                  >
                                    ✓
                                  </button>
                                  <button
                                    className="text-3xl font-bold text-(--secondary-color) cursor-pointer"
                                    onClick={async () => {
                                      await rejectApplication(token, application.project.id, application.application_id);
                                      setRender(render+1);}
                                    }
                                    title="Decline"
                                  >
                                    ✗
                                  </button>
                                </>
                              )}
                              {application.status === "APPROVED" && (
                                <span className="text-(--secondary-color) font-bold hover:underline cursor-pointer"
                                onClick={async () => {
                                  await returnApplication(token, application.project.id, application.application_id);
                                  setRender(render+1);}
                                }
                                >Added</span>
                              )}
                              {application.status === "REJECTED" && (
                                <span className="text-(--secondary-color) font-bold hover:underline cursor-pointer"
                                onClick={async () => {
                                  await returnApplication(token, application.project.id, application.application_id);
                                  setRender(render+1);}
                                }
                                >Declined</span>
                              )}
                            </div>
                        </div>
                      );
                    })}
                  </div>
                )
              }
          </Popup>
        :
        <p>0 applications</p>
        }
      </div>
    </div>
  );
}