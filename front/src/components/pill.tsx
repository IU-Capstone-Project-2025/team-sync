import {useState} from 'react'

export default function Pill({type, number} : {type : string, number : number}) {
  const [isActive, setActive] = useState(false);
  return (
    <div className={'cursor-pointer rounded-2xl flex flex-row max-w-fit items-baseline px-2 py-0.5 ' + (isActive ? 'bg-(--primary-color)' : 'bg-(--header-footer-color)')} onClick={() => setActive(!isActive)}>
      <p className={'font-[Inter] text-lg font-bold mr-2 ' + (isActive ? 'text-(--header-footer-color)'
         : 'text-(--primary-color)')}>
        {type}
      </p>
      <p className={'font-[Inter] text-md ' + (isActive ? 'text-(--header-footer-color)' : 'text-(--primary-color)')}>
        {number}
      </p>
    </div>
  );
}