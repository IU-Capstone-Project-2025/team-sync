import Footer from "../components/footer"
import { useNavigate } from "react-router-dom";
import HomeHeader from "../components/homeHeader";
import { useEffect, useState } from "react";
import CustomizedHook from "../components/autocompleteInput";
import { getRoles, getSkills } from "../utils/backendFetching";


interface Person {
    id: number,
    name: string,
    surname: string,
    email: string
}

interface StudentProfile {
    study_group: {
        id: number,
        name: string
    },
    description: string,
    github_alias: string,
    tg_alias: string,
    skills: number[],
    roles: number[]
}

interface UserProfile {
    person: Person,
    student_profile: StudentProfile
}

const backendHost = import.meta.env.VITE_BACKEND_HOST

async function getUserSkills(token: string, userId: number) {
    const skillsUrl = `${backendHost}/resume/api/v1/profile/student/${userId}/skills`

    const response = await fetch(skillsUrl, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        throw new Error('Response error: ' + response.status.toString());
    }

    const responseJson = await response.json();
    return responseJson.data.content;
}

async function getUserRoles(token: string, userId: number) {
    const skillsUrl = `${backendHost}/resume/api/v1/profile/student/${userId}/roles?`

    const response = await fetch(skillsUrl, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        throw new Error('Response error: ' + response.status.toString());
    }
    
    const responseJson = await response.json();
    return responseJson.data.content;
}


async function getProfile(token: string) {
    const profileUrl = `${backendHost}/resume/api/v1/profile`;
    const response = await fetch(profileUrl, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        throw new Error('Response error: ' + response.status.toString());
    }
    const responseJson = await response.json();
    return responseJson.data;
}

export default function ProfileScreen() {
    const [userProfile, setUserProfile] = useState<UserProfile>()
    const [userSkills, setUserSkills] = useState<{ id: number, name: string }[]>([]);
    const [userRoles, setUserRoles] = useState<{ id: number, name: string }[]>([]);
    
    const [tgAlias, setTgAlias] = useState('');
    const [githubAlias, setGithubAlias] = useState('');
    const [studyGroupName, setStudyGroupName] = useState('');
    const [description, setDescription] = useState('');

    const [roles, setRoles] = useState<{ id: number, name: string }[]>([]);
    const [skills, setSkills] = useState<{ id: number, name: string }[]>([]);

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("backendToken")
        if (token) {
            try {
                getProfile(token).then(result => {
                    setUserProfile(result)
                    setTgAlias(result.student_profile.tg_alias);
                    setGithubAlias(result.student_profile.github_alias);
                    setStudyGroupName(result.student_profile.study_group.name);
                    setDescription(result.student_profile.description);

                    getUserSkills(token, result.person.id).then(skills => {
                        setUserSkills(skills.map(skill => ({ id: skill.id, name: skill.name })))
                    })

                    getUserRoles(token, result.person.id).then(roles => {
                        setUserRoles(roles.map(role => ({ id: role.id, name: role.name })))
                    })

                    getSkills(token).then(skills => {
                        setSkills(skills.map(skill => ({ id: skill.id, name: skill.name })))
                    })

                    getRoles(token).then(roles => {
                        setRoles(roles.map(role => ({ id: role.id, name: role.name })))
                    })
                })

  
            } catch (error) {
                console.error(error.message);
            }
        }
    }, [])

    async function updateProfile(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const updateProfileUrl = `${backendHost}/resume/api/v1/profile/student/${userProfile?.person.id}`;
        const profileJson = {
            "tg_alias": formData.get('tgAlias'),
            "github_alias": formData.get('githubAlias'),
            "study_group": formData.get('studyGroup'),
            "description": formData.get('description'),
            "skills": userSkills.map(skill => skill.id),
            "roles": userRoles.map(role => role.id)
        };

        const token = localStorage.getItem("backendToken");
        if (!token) {
            console.error("No token found");
            return;
        }
        
        const response = await fetch(updateProfileUrl, {
            method: 'PUT',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(profileJson)
        });

        if(!response.ok) {
            alert('Failed to update profile: '+response.statusText);
        } else {
            alert('Profile updated successfully');
        }
    }

    return (
        <div className="flex flex-col min-h-screen">
            <HomeHeader />
            <div className="pl-16 pt-10 flex-1">
                <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">Your Profile</h1>
                <form onSubmit={updateProfile}>
                    <p className="mb-2">Telegram Alias</p>
                    <input name="tgAlias" required value={tgAlias} onChange={e => setTgAlias(e.target.value)} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">GitHub Alias</p>
                    <input name="githubAlias" required value={githubAlias} onChange={e => setGithubAlias(e.target.value)} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">Study Group</p>
                    <input name="studyGroup" required value={studyGroupName} onChange={e => setStudyGroupName(e.target.value)} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">About</p>
                    <input name="description" required value={description} onChange={e => setDescription(e.target.value)} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">Your roles</p>
                    <CustomizedHook
                        arr ={roles}
                        value={roles.filter(role => userRoles.map(s => s.id).includes(role.id))}
                        onChange={items => setUserRoles(items.map(item => ({ id: item.id, name: item.name })))}
                    />

                    <p className="mb-2">Your skills</p>
                    <CustomizedHook
                        arr ={skills}
                        value={skills.filter(skill => userSkills.map(s => s.id).includes(skill.id))}
                        onChange={items => setUserSkills(items.map(item => ({ id: item.id, name: item.name })))}
                    />
                    <br/>
                    <button className="bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl mb-5" type="submit">
                        Update Profile
                    </button>
                </form>
            </div>
            <Footer />
        </div>
    );
}