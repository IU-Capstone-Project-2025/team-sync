import Footer from "../components/footer";
import HomeHeader from "../components/homeHeader";
import Popup from "reactjs-popup";
import 'reactjs-popup/dist/index.css';
import { useEffect, useState } from "react";
import Card from "../components/card";
import Checkbox from "@mui/material/Checkbox";
import ClearIcon from '@mui/icons-material/Clear';
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

const backendHost = import.meta.env.VITE_BACKEND_HOST

async function getRoles(token: string) {
  const rolesUrl = `${backendHost}/projects/api/v1/roles`;
  try {
    const response = await fetch(rolesUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content.map((role) => ({ id: role.id, name: role.name }));
  }
  catch (error) {
    console.error(error.message);
  }
}

async function getSkills(token: string) {
  const rolesUrl = "/projects/api/v1/skills";
  try {
    const response = await fetch(rolesUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.content.map((skill) => ({ id: skill.id, name: skill.name }));
  }
  catch (error) {
    console.error(error.message);
  }
}

async function getProjects(
  token: string,
  filterSkills: {id: number, name: string}[],
  filterRoles: {id: number, name: string}[]
) {
  let params: string[] = [];
  if (filterSkills.length > 0) {
    params.push("skillIds=" + filterSkills.map(skill => skill.id).join(','));
  }
  if (filterRoles.length > 0) {
    params.push("roleIds=" + filterRoles.map(role => role.id).join(','));
  }
  const queryString = params.length > 0 ? "&" + params.join("&") : "";
  const projectsUrl = "/projects/api/v1/projects?sort=id,desc&size=50" + queryString;
  try {
    const response = await fetch(projectsUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return {
      projects: json.data.content,
      total: json.data.number_of_elements
    }
  }
  catch (error) {
    console.error(error.message);
    return {
      projects: [],
      totalProjects: 0
    };
  }
}

function getNames(ids: number[] = [], all: {id: number, name: string}[] = []) {
  const names = ids.map(id => all.find(obj => obj.id === id)?.name ?? "Unknown");
  return names;
}

export default function HomeScreen(){
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const [filterCourse, setFilterCourse] = useState<string>("");
  const [filterSkills, setFilterSkills] = useState<{id: number, name: string}[]>([]);
  const [filterRoles, setFilterRoles] = useState<{id: number, name: string}[]>([]);
  const [numProjects, setNumProjects] = useState<number>(0);
  

  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token){
      getProjects(token, filterSkills, filterRoles).then((result => {
        setProjects(result.projects);
        setNumProjects(result.total);
      }));
      getRoles(token).then(setRoles);
      getSkills(token).then(setSkills);
    }
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("backendToken");
    if (token){
      getProjects(token, filterSkills, filterRoles).then((result => {
        setProjects(result.projects);
        setNumProjects(result.total);
      }));
      console.log(projects);
    }
  }, [filterCourse, filterSkills, filterRoles]);
  return (
    <div className="flex flex-col justify-between min-h-screen">
      <HomeHeader />
      <div className="flex flex-col px-18">
          <p className="font-[Inter] text-(--primary-color) text-xl pt-3 pb-5">
            All projects
          </p>
          <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold">
            Projects
          </h1>
          <div className="flex flex-row">
            <div className="bg-(--header-footer-color) rounded-2xl p-4 mt-5 min-w-90 h-fit">
              <div className="mb-5">

                <h3 className="font-[Inter] text-(--secondary-color) font-bold text-xl">
                  Courses
                </h3>
                <h4 className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg mt-3 px-3 py-1 w-fit">
                  All courses
                </h4>
                <Popup
                  trigger={
                    <button className="hover:underline font-[Inter] font-bold mt-1 ml-0.5 text-(--primary-color)">
                      Change
                    </button>
                  }
                  modal
                >
                  {
                    // @ts-ignore
                    (close) => (
                      <div className="modal rounded-2xl text-black bg-(--header-footer-color)">
                        <button className="close" onClick={close}> &times;</button>
                        <div style={{ maxHeight: '500px', overflowY: 'auto', paddingRight: '4px' }}>
                          <h2>Choose course</h2>
                          
                          // TODO
                        </div>
                      </div>
                    )
                  }
                </Popup>
              </div>


              <div className="mb-5">
                <h3 className="font-[Inter] text-(--secondary-color) font-bold text-xl">
                  Skills
                </h3>
                {filterSkills.length === 0 ? (
                  <h4 className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg mt-3 px-3 py-1 w-fit">
                    All skills
                  </h4>
                ) : (
                  <div className="flex flex-wrap gap-2 mt-3">
                    {filterSkills.map(skill => (
                      <span key={skill.id} className="flex justify-between align-center font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg px-3 pr-1 py-1 w-fit gap-0.5">
                        {skill.name}
                        <ClearIcon
                        sx={{
                          padding: 0,
                          color: "var(--primary-color)",
                          '&.Mui-checked': {
                            color: "var(--accent-color-2)",
                          }
                          }}
                          onClick = {() => {
                            const skillsAfterDeletion = filterSkills.filter((skill2) => skill2 != skill);
                            setFilterSkills(skillsAfterDeletion);
                            console.log(filterSkills);
                            }}/>
                      </span>
                    ))}
                  </div>
                )}
                <Popup
                  trigger={
                    <button className="hover:underline font-[Inter] font-bold mt-1 ml-0.5 text-(--primary-color)">
                      Change
                    </button>
                  }
                  modal
                >
                  {
                    // @ts-ignore
                    (close) => {
                      const [selectedSkills, setSelectedSkills] = useState<number[]>(
                        filterSkills.map(skill => skill.id)
                      );
                      useEffect(() => {
                        setSelectedSkills(filterSkills.map(skill => skill.id));
                      }, [filterSkills]);

                      const handleSkillToggle = (id: number) => {
                        setSelectedSkills(prev =>
                          prev.includes(id) ? prev.filter(sid => sid !== id) : [...prev, id]
                        );
                      };

                      const handleSubmit = () => {
                        setFilterSkills(skills.filter(skill => selectedSkills.includes(skill.id)));
                        close();
                      };

                      return (
                        <div className="flex flex-col modal rounded-2xl text-(--secondary-color) bg-(--header-footer-color)">
                          <button className="close self-end mr-3 text-(--secondary-color) text-4xl" onClick={close}> &times;</button>
                          <div className="flex flex-col p-18">
                            <h2 className="font-[Inter] font-bold text-4xl pb-2">
                              Choose skills
                            </h2>
                            <div style={{ maxHeight: '50vh', overflowY: 'auto', paddingRight: '4px' }}>
                              {skills.length > 0 && skills.map((skill) => {
                                const isChecked = selectedSkills.includes(skill.id);
                                return (
                                  <button
                                    key={skill.id}
                                    type="button"
                                    onClick={() => handleSkillToggle(skill.id)}
                                    className={
                                      "inline-flex flex-row items-center font-[Inter] text-(--secondary-color) border-2 rounded-lg mt-3 px-2 mx-1 py-1 w-fit" +
                                      (isChecked ? "border-(--accent-color-2)" : "border-(--primary-color)")
                                    }
                                  >
                                    <Checkbox
                                      checked={isChecked}
                                      size="small"
                                      sx={{
                                        padding: 0,
                                        color: "var(--primary-color)",
                                        '&.Mui-checked': {
                                          color: "var(--accent-color-2)",
                                        }
                                      }}
                                    />
                                    <span className="ml-2">{skill.name}</span>
                                  </button>
                                );
                              })}
                            </div>
                            <button
                              className="mt-6 px-4 py-2 bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl text-xl w-fit"
                              onClick={handleSubmit}
                            >
                              Add skills
                            </button>
                          </div>
                        </div>
                      );
                    }
                  }
                </Popup>
              </div>


              <div className="mb-5">
                <h3 className="font-[Inter] text-(--secondary-color) font-bold text-xl">
                  Roles
                </h3>
                {filterRoles.length === 0 ? (
                  <h4 className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg mt-3 px-3 py-1 w-fit">
                    All roles
                  </h4>
                ) : (
                  <div className="flex flex-wrap gap-2 mt-3">
                    {filterRoles.map(role => (
                      <span key={role.id} className="flex justify-between align-center font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg px-3 pr-1 py-1 w-fit gap-0.5">
                        {role.name}
                        <ClearIcon
                          sx={{
                            padding: 0,
                            color: "var(--primary-color)",
                            '&.Mui-checked': {
                              color: "var(--accent-color-2)",
                            }
                          }}
                          onClick={() => {
                            const rolesAfterDeletion = filterRoles.filter((role2) => role2 !== role);
                            setFilterRoles(rolesAfterDeletion);
                          }}
                        />
                      </span>
                    ))}
                  </div>
                )}
                <Popup
                  trigger={
                    <button className="hover:underline font-[Inter] font-bold mt-1 ml-0.5 text-(--primary-color)">
                      Change
                    </button>
                  }
                  modal
                >
                  {
                    // @ts-ignore
                    (close) => {
                      const [selectedRoles, setSelectedRoles] = useState<number[]>(
                        filterRoles.map(role => role.id)
                      );
                      useEffect(() => {
                        setSelectedRoles(filterRoles.map(role => role.id));
                      }, [filterRoles]);

                      const handleRoleToggle = (id: number) => {
                        setSelectedRoles(prev =>
                          prev.includes(id) ? prev.filter(rid => rid !== id) : [...prev, id]
                        );
                      };

                      const handleSubmit = () => {
                        setFilterRoles(roles.filter(role => selectedRoles.includes(role.id)));
                        close();
                      };

                      return (
                        <div className="flex flex-col modal rounded-2xl text-(--secondary-color) bg-(--header-footer-color)">
                          <button className="close self-end mr-3 text-(--secondary-color) text-4xl" onClick={close}> &times;</button>
                          <div className="flex flex-col p-18">
                            <h2 className="font-[Inter] font-bold text-4xl">
                              Choose roles
                            </h2>
                            <div style={{ maxHeight: '50vh', overflowY: 'auto', paddingRight: '4px' }}>
                              {roles.length > 0 && roles.map((role) => {
                                const isChecked = selectedRoles.includes(role.id);
                                return (
                                  <button
                                    key={role.id}
                                    type="button"
                                    onClick={() => handleRoleToggle(role.id)}
                                    className={
                                      "inline-flex flex-row items-center font-[Inter] text-(--secondary-color) border-2 rounded-lg mt-3 px-2 mx-1 py-1 w-fit " +
                                      (isChecked ? "border-(--accent-color-2)" : "border-(--primary-color)")
                                    }
                                  >
                                    <Checkbox
                                      checked={isChecked}
                                      onChange={() => handleRoleToggle(role.id)}
                                      size="small"
                                      sx={{
                                        padding: 0,
                                        color: "var(--primary-color)",
                                        '&.Mui-checked': {
                                          color: "var(--accent-color-2)",
                                        }
                                      }}
                                    />
                                    <span className="ml-2">{role.name}</span>
                                  </button>
                                );
                              })}
                            </div>
                            <button
                              className="mt-6 px-4 py-2 bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl text-xl w-fit"
                              onClick={handleSubmit}
                            >
                              Add roles
                            </button>
                          </div>
                        </div>
                      );
                    }
                  }
                </Popup>
              </div>
            </div>
            <div className="flex flex-col ml-15">
              <p className="font-[Inter] text-lg mt-5 text-(--primary-color)">
                {numProjects} projects found
              </p>
              {projects.length > 0 && roles.length > 0 && projects.map((proj, idx) => (
                <Card
                  key={proj.id || idx}
                  props={{
                    courseName: proj.course_name,
                    description: proj.description,
                    id: proj.id,
                    projectName: proj.name,
                    roles: getNames(proj.role_ids, roles),
                    skills: getNames(proj.skill_ids, skills),
                    status: proj.status,
                    teamLeadId: proj.team_lead_id
                  }}
                />
              ))}
            </div>
          </div>
      </div>
      <Footer/>
    </div>
  );
}