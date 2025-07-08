import Footer from "../components/footer"
import { useNavigate } from "react-router-dom";
import HomeHeader from "../components/homeHeader";
import { useEffect, useState } from "react";

interface Person{
    id: number,
    name: string,
    surname: string,
    email: string
}

interface StudentProfile{
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

interface UserProfile{
    person: Person,
    student_profile: StudentProfile
}

const backendHost = import.meta.env.VITE_BACKEND_HOST

async function getProfile(token: string) {
    const profileUrl = `${backendHost}/resume/api/v1/profile`;
    try {
        const response = await fetch(profileUrl, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if(!response.ok){
            throw new Error('Response error: ' + response.status.toString());
        }
        const responseJson = await response.json();
        return responseJson.data;
    } catch (error) {
        console.error(error.message);
    }
}

export default function ProfileScreen() {
    const [userProfle, setUserProfile] = useState<UserProfile>()

    const navigate = useNavigate();

    useEffect(()=>{
        const token = localStorage.getItem("backendToken")
        if(token){
            getProfile(token).then(result => {
                setUserProfile(result)
            })
        }
    },[])

    async function updateProfile(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const profile = `${backendHost}/projects/api/v1/projects`;
        alert("Profile updated successfully");
    }

    return (
        <div className="flex flex-col min-h-screen">
            <HomeHeader />
            <div className="pl-16 pt-10 flex-1">
                <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">Your Profile</h1>
                <form onSubmit={updateProfile}>

                    <p className="mb-2">Name</p>
                    <input name="tgAlias" required value={userProfle?.student_profile.tg_alias} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">Surname</p>
                    <input name="githubAlias" required value={userProfle?.student_profile.github_alias} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">Study Group</p>
                    <input name="email" required value={userProfle?.student_profile.study_group.name} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />

                    <p className="mb-2">About me</p>
                    <input name="studyGroup" required value={userProfle?.student_profile.description} className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" />


                    <p className="mb-2">Required roles</p>
                    {/* <CustomizedHook
                arr={roles}
                value={roles.filter(role => selectedRoles.includes(role.id))}
                onChange={items => setSelectedRoles(items.map(item => item.id))}
            /> */}
                    <p className="mb-2 mt-5">How many people do you need?</p>
                    <input name="numPeople" required className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
                    <p className="mb-2">Required skills</p>
                    {/* <CustomizedHook
                arr={skills}
                value={skills.filter(skill => selectedSkills.includes(skill.id))}
                onChange={items => setSelectedSkills(items.map(item => item.id))}
            /> */}
                    <p className="mb-2 mt-5">Project link</p>
                    <input name="projectLink" required className="focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="url" />
                    <button className="bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl mb-5" type="submit">
                        Add project
                    </button>
                </form>
            </div>
            <Footer />
        </div>
    );
}