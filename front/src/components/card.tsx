export default function Card({props}) {
  return (
    <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl" style={{marginRight: '1vw', marginLeft: '1vw'}}>
      <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projName}</h1>
      <p className="pb-4 font-[Inter] text-xl max-w-[70%]">{props.description}</p>
      <div className="flex flex-row justify-between pr-3">
        <p className="font-[Inter] text-lg"><em className="font-bold">Available roles:</em> {props.roles.join(", ")}</p>
        <p className="font-[Inter] font-bold text-xl">{props.numPeople}</p>
      </div>
    </div>
  );
}