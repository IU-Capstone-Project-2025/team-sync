const backendHost = import.meta.env.VITE_BACKEND_HOST

export async function getSkills() {
  const skillsUrl = `${backendHost}/resume/api/v1/skills`;
  try {
    const response = await fetch(skillsUrl, {
      headers: {
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.content.map((skill) => ({ id: skill.id, name: skill.name, description: skill.description}));
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function getRoles() {
  const rolesUrl = `${backendHost}/resume/api/v1/roles`;
  try {
    const response = await fetch(rolesUrl, {
      headers: {
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.content.map((role) => ({ id: role.id, name: role.name, description: role.description}));
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function getCourses(token: string) {
  const coursesUrl = `${backendHost}/projects/api/v1/courses`;
  try {
    const response = await fetch(coursesUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data.map((course) => ({ id: course.id, name: course.name }));
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function getProjects(
  token: string,
  filterSkills: {id: number, name: string}[],
  filterRoles: {id: number, name: string}[],
  filterCourse: string
) {
  let params: string[] = [];
  if (filterSkills.length > 0) {
    params.push("skillIds=" + filterSkills.map(skill => skill.id).join(','));
  }
  if (filterRoles.length > 0) {
    params.push("roleIds=" + filterRoles.map(role => role.id).join(','));
  }
  if (filterCourse !== "") {
    params.push("courseIds=" + filterCourse);
  }
  const queryString = params.length > 0 ? "&" + params.join("&") : "";
  const projectsUrl = `${backendHost}/projects/api/v1/projects/recommendations?sort=id,desc&size=200` + queryString;
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
      total: json.data.content.length
    }
  }
  catch (error) {
    console.error(error.message);
    return {
      projects: []
    };
  }
}

export async function getRecs(
  token: string
) {
  const projectsUrl = `${backendHost}/projects/api/v1/projects/recommendations?size=200`;
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
      total: 0
    };
  }
}

export async function likeProject(projId: number, token: string){
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

export async function unlikeProject(projId: number, token: string){
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

export async function getLikedProjects(token: string) {
  const projectsUrl = `${backendHost}/projects/api/v1/favourite/my`;
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
    return json.data.content;
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function getApplications(token: string) {
  const applicationsUrl = `${backendHost}/projects/api/v1/applications/my`;
  try {
    const response = await fetch(applicationsUrl, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    if (json.data.total_elements == 0){return [];}
    return json.data.content;
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function getMyProjects(token: string) {
  const projectsUrl = backendHost + "/projects/api/v1/projects/my";
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
      projects: json.data.content
    }
  }
  catch (error) {
    console.error(error.message);
    return {
      projects: []
    };
  }
}

export async function deleteApplication(token: string, applicationId: number) : Promise<boolean>{
  const applicationUrl = `${backendHost}/projects/api/v1/applications/${applicationId}`;
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

export async function getProjectApplications(token: string, projectId: number) {
  const projectUrl = `${backendHost}/projects/api/v1/projects/${projectId}/applications`;
  try {
    const response = await fetch(projectUrl, {
      mode: 'cors',
      headers: {
        "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.content
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function getPersonById(token: string, personId: number){
  const personUrl = `${backendHost}/resume/api/v1/profile/${personId}`;
  try {
    const response = await fetch(personUrl, {
      mode: 'cors',
      headers: {
        "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
      }
    });
    if (!response.ok) {
      throw new Error('Response error: ' + response.status.toString());
    }
    const json = await response.json();
    return json.data;
  }
  catch (error) {
    console.error(error.message);
    return [];
  }
}

export async function approveApplication(token: string, projectId: number, applicationId: number){
  const appJson = {
    status: "APPROVED"
  }
  const applicationUrl = `${backendHost}/projects/api/v1/projects/${projectId}/applications/${applicationId}`;
  try {
    const response = await fetch(applicationUrl, {
      method: "PATCH",
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

export async function rejectApplication(token: string, projectId: number, applicationId: number){
  const appJson = {
    status: "REJECTED"
  }
  const applicationUrl = `${backendHost}/projects/api/v1/projects/${projectId}/applications/${applicationId}`;
  try {
    const response = await fetch(applicationUrl, {
      method: "PATCH",
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

export async function returnApplication(token: string, projectId: number, applicationId: number){
  const appJson = {
    status: "PENDING"
  }
  const applicationUrl = `${backendHost}/projects/api/v1/projects/${projectId}/applications/${applicationId}`;
  try {
    const response = await fetch(applicationUrl, {
      method: "PATCH",
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

export function getNames(ids: number[] = [], all: {id: number, name: string}[] = []) {
  const names = ids.map(id => all.find(obj => obj.id === id)?.name ?? "Unknown");
  return names;
}