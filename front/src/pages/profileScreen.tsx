import Footer from "../components/footer"
import { useNavigate } from "react-router-dom";
import HomeHeader from "../components/homeHeader";

export default function ProfileScreen() {
    const navigate = useNavigate();

    return (
    <div className="flex flex-col min-h-screen">
        <HomeHeader/>
        <div className="pl-16 pt-10 flex-1">
            <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">New project</h1>
            <form onSubmit={createProject}>
            <p className="mb-2">Name</p>
            <input name = "projectName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
            <p className="mb-2">Description</p>
            <input name = "description" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
            <p className="mb-2">Course</p>
            <input name = "courseName" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
            <p className="mb-2">Required roles</p>
            <CustomizedHook
                arr={roles}
                value={roles.filter(role => selectedRoles.includes(role.id))}
                onChange={items => setSelectedRoles(items.map(item => item.id))}
            />
            <p className="mb-2 mt-5">How many people do you need?</p>
            <input name = "numPeople" required className = "focus:border-(--accent-color-2) focus:outline-none mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
            <p className="mb-2">Required skills</p>
            <CustomizedHook
                arr={skills}
                value={skills.filter(skill => selectedSkills.includes(skill.id))}
                onChange={items => setSelectedSkills(items.map(item => item.id))}
            />
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