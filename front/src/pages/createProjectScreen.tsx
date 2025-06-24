import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer";

export default function CreateProjectScreen() {
  return (
    <div className="flex flex-col min-h-screen">
      <HomeHeader/>
      <div className="pl-16 pt-10 flex-1">
        <h1 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold mb-11">New project</h1>
        <form action="query">
          <p className="mb-2">Name</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Description</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Course</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Required roles</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">How many people do you need?</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="number" />
          <p className="mb-2">Required skills</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="query" />
          <p className="mb-2">Project link</p>
          <input required className = "mb-5 border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md" type="url" />
          <button className = "bg-(--accent-color-2)/42 text-(--secondary-color) rounded-2xl p-2 px-4 block text-xl mb-5" type="submit">
            Add project
          </button>
        </form>
      </div>
      <Footer/>
    </div>
  );
}