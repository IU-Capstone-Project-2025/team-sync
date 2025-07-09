const backendHost = import.meta.env.VITE_BACKEND_HOST

export async function getSkills(token:string) {
  const skillsUrl = `${backendHost}/projects/api/v1/skills`;
  try {
    const response = await fetch(skillsUrl, {
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

export async function getRoles(token:string) {
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