export default function HomeFilter(){
  return (
    <div className="bg-(--header-footer-color) rounded-2xl p-4 mt-5 min-w-90 max-h-100">
      <div className="mb-5">
        <h3 className="font-[Inter] text-(--secondary-color) font-bold text-xl">
          Courses
        </h3>
        <h4 className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg mt-3 px-3 py-1 w-fit">
          All courses
        </h4>
        <p className="font-[Inter] font-bold mt-1 ml-0.5 text-(--primary-color)">
          Change
        </p>
      </div>
      <div className="mb-5">
        <h3 className="font-[Inter] text-(--secondary-color) font-bold text-xl">
          Skills
        </h3>
        <h4 className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg mt-3 px-3 py-1 w-fit">
          All skills
        </h4>
        <p className="font-[Inter] font-bold mt-1 ml-0.5 text-(--primary-color)">
          Change
        </p>
      </div>
      <div className="mb-5">
        <h3 className="font-[Inter] text-(--secondary-color) font-bold text-xl">
          Roles
        </h3>
        <h4 className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg mt-3 px-3 py-1 w-fit">
          All roles
        </h4>
        <p className="font-[Inter] font-bold mt-1 ml-0.5 text-(--primary-color)">
          Change
        </p>
      </div>
    </div>
  );
}