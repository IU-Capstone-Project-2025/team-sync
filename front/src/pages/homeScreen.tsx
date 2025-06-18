import Footer from "../components/footer";
import HomeHeader from "../components/homeHeader";
import HomeFilter from "../components/homeFilter";

export default function HomeScreen(){
  return (
    <div className="flex flex-col justify-between min-h-screen">
      <HomeHeader />
      <div className="flex flex-col px-18">
          <p className="font-[Inter] text-(--primary-color) text-xl pb-5">
            All projects
          </p>
          <h2 className="font-[Manrope] text-(--secondary-color) text-5xl font-bold">
            Projects
          </h2>
          <div className="flex flex-row">
            <HomeFilter/>
            <p className="font-[Inter] text-lg ml-15 mt-5 text-(--primary-color)">
              2 projects found
            </p>
          </div>
      </div>
      <Footer/>
    </div>
  );
}