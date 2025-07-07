import Footer from "../components/footer"
import { useNavigate } from "react-router-dom";
import HomeHeader from "../components/homeHeader";

const backendHost = import.meta.env.VITE_BACKEND_HOST

export default function ProfileScreen() {
    const navigate = useNavigate();

    async function updateProfile(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const profile = `${backendHost}/projects/api/v1/projects`;
        alert("Profile updated successfully");
        // if (selectedSkills.length === 0 || selectedRoles.length === 0){
        // console.error("Skills or roles empty");
        // return;
        // }
        // const projJson = {
        // "course_name": formData.get('courseName'),
        // "description": formData.get('description'),
        // "name": formData.get('projectName'),
        // "skills": selectedSkills,
        // "roles": selectedRoles,
        // "status": "OPEN"
        // };

        // const token = localStorage.getItem("backendToken");

        // const response = await fetch(projectUrl, {  
        // method: 'POST', 
        // mode: 'cors', 
        // headers: {
        //     "Content-Type": "application/json",
        //     "Authorization": `Bearer ${token}`
        // },
        // body: JSON.stringify(projJson) 
        // });
        // if (response.ok) {
        // navigate('/home');
        // } else {
        // alert("Failed to create project");
        // }
    }
    
    return (
    <div className="flex flex-col min-h-screen">
        <HomeHeader/>
        <div className="pl-16 pt-10 flex-1">
            <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">Your Profile</h1>
            <form onSubmit={updateProfile}>

            <p className="mb-2">Name</p>
            <input name = "personName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />

            <p className="mb-2">Surname</p>
            <input name = "personSurname" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />

            <p className="mb-2">Course</p>
            <input name = "courseName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />

            <p className="mb-2">Required roles</p>
            {/* <CustomizedHook
                arr={roles}
                value={roles.filter(role => selectedRoles.includes(role.id))}
                onChange={items => setSelectedRoles(items.map(item => item.id))}
            /> */}
            <p className="mb-2 mt-5">How many people do you need?</p>
            <input name = "numPeople" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
            <p className="mb-2">Required skills</p>
            {/* <CustomizedHook
                arr={skills}
                value={skills.filter(skill => selectedSkills.includes(skill.id))}
                onChange={items => setSelectedSkills(items.map(item => item.id))}
            /> */}
            <p className="mb-2 mt-5">Project link</p>
            <input name = "projectLink" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="url" />
            <button className = "bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl mb-5" type="submit">
                Add project
            </button>
            </form>
        </div>
        <Footer/>
    </div>
    );
}