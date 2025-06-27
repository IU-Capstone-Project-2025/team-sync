function truncateString({string, maxLength} : {string: string, maxLength: number}){
  if (string.length >  maxLength){
    return string.substring(0, maxLength-1) + "...";
  }
  else {
    return string;
  }
}

export default function Card({props}) {
  return (
    <div className="mt-4 bg-(--header-footer-color) w-full max-w-[98vw] text-(--secondary-color) p-5 border-(--secondary-color) border-1 rounded-2xl">
      <h1 className="pb-4 font-[Manrope] font-extrabold text-4xl">{props.projName}</h1>
      <p className="pb-4 font-[Inter] text-xl max-w-[70%]">{truncateString({ string: props.description, maxLength: 350 })}</p>
      <p className="font-[Inter] text-lg"><em className="font-bold">Available roles:</em> {props.roles.join(", ")}</p>
    </div>
  );
}